package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.sc.smartcommunitybackend.domain.MockPaymentRecord;
import org.sc.smartcommunitybackend.domain.PaymentOrder;
import org.sc.smartcommunitybackend.domain.PropertyFeePayment;
import org.sc.smartcommunitybackend.domain.WalletTransaction;
import org.sc.smartcommunitybackend.dto.response.PaymentResponse;
import org.sc.smartcommunitybackend.enums.OrderType;
import org.sc.smartcommunitybackend.enums.PaymentMethod;
import org.sc.smartcommunitybackend.enums.PaymentOrderStatus;
import org.sc.smartcommunitybackend.exception.BusinessException;
import org.sc.smartcommunitybackend.mapper.MockPaymentRecordMapper;
import org.sc.smartcommunitybackend.mapper.PaymentOrderMapper;
import org.sc.smartcommunitybackend.service.PaymentService;
import org.sc.smartcommunitybackend.service.PropertyFeeService;
import org.sc.smartcommunitybackend.service.WalletService;
import org.sc.smartcommunitybackend.util.TransactionNoGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * 支付服务实现类
 */
@Service
public class PaymentServiceImpl implements PaymentService {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
    
    @Autowired
    private PaymentOrderMapper paymentOrderMapper;
    
    @Autowired
    private MockPaymentRecordMapper mockPaymentRecordMapper;
    
    @Autowired
    private WalletService walletService;
    
    @Autowired
    private PropertyFeeService propertyFeeService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentOrder createPaymentOrder(Long userId, String orderType, BigDecimal amount,
                                          String paymentMethod, Long relatedId, String description) {
        // 参数校验
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("支付金额必须大于0");
        }
        
        // 创建支付订单
        PaymentOrder order = new PaymentOrder();
        order.setOrderNo(TransactionNoGenerator.generatePaymentOrderNo());
        order.setUserId(userId);
        order.setOrderType(orderType);
        order.setAmount(amount);
        order.setPaymentMethod(paymentMethod);
        order.setStatus(PaymentOrderStatus.PENDING.getCode());
        order.setRelatedId(relatedId);
        order.setDescription(description);
        
        // 设置过期时间（30分钟）
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + 30 * 60 * 1000);
        order.setExpireTime(expireTime);
        
        order.setCreateTime(now);
        order.setUpdateTime(now);
        
        paymentOrderMapper.insert(order);
        
        logger.info("创建支付订单：订单号 {}，用户 {}，金额 {}，支付方式 {}", 
                order.getOrderNo(), userId, amount, paymentMethod);
        
        return order;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentResponse initiatePayment(String orderNo) {
        // 查询订单
        LambdaQueryWrapper<PaymentOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentOrder::getOrderNo, orderNo);
        PaymentOrder order = paymentOrderMapper.selectOne(wrapper);
        
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        if (!order.getStatus().equals(PaymentOrderStatus.PENDING.getCode())) {
            throw new BusinessException("订单状态不正确");
        }
        
        // 检查是否过期
        if (order.getExpireTime().before(new Date())) {
            order.setStatus(PaymentOrderStatus.CANCELLED.getCode());
            order.setUpdateTime(new Date());
            paymentOrderMapper.updateById(order);
            throw new BusinessException("订单已过期");
        }
        
        // 更新订单状态为支付中
        order.setStatus(PaymentOrderStatus.PAYING.getCode());
        order.setUpdateTime(new Date());
        paymentOrderMapper.updateById(order);
        
        // 根据支付方式生成不同的响应
        PaymentResponse.PaymentResponseBuilder responseBuilder = PaymentResponse.builder()
                .orderNo(order.getOrderNo())
                .paymentMethod(order.getPaymentMethod())
                .amount(order.getAmount())
                .status(order.getStatus());
        
        if (PaymentMethod.ALIPAY.name().equals(order.getPaymentMethod())) {
            // 模拟支付宝支付
            String thirdPartyOrderNo = "ALIPAY_" + UUID.randomUUID().toString().replace("-", "");
            order.setThirdPartyOrderNo(thirdPartyOrderNo);
            paymentOrderMapper.updateById(order);
            
            responseBuilder.thirdPartyOrderNo(thirdPartyOrderNo)
                    .paymentUrl("mock://alipay/pay?orderNo=" + orderNo)
                    .message("请在支付宝中完成支付（模拟）");
                    
        } else if (PaymentMethod.WECHAT.name().equals(order.getPaymentMethod())) {
            // 模拟微信支付
            String thirdPartyOrderNo = "WECHAT_" + UUID.randomUUID().toString().replace("-", "");
            order.setThirdPartyOrderNo(thirdPartyOrderNo);
            paymentOrderMapper.updateById(order);
            
            responseBuilder.thirdPartyOrderNo(thirdPartyOrderNo)
                    .paymentUrl("mock://wechat/pay?orderNo=" + orderNo)
                    .message("请在微信中完成支付（模拟）");
                    
        } else if (PaymentMethod.WALLET.name().equals(order.getPaymentMethod())) {
            // 钱包支付直接处理
            responseBuilder.message("钱包支付");
        }
        
        logger.info("发起支付：订单号 {}，支付方式 {}", orderNo, order.getPaymentMethod());
        
        return responseBuilder.build();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean mockPaymentCallback(String orderNo, boolean success) {
        // 查询订单
        LambdaQueryWrapper<PaymentOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentOrder::getOrderNo, orderNo);
        PaymentOrder order = paymentOrderMapper.selectOne(wrapper);
        
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        if (!order.getStatus().equals(PaymentOrderStatus.PAYING.getCode())) {
            throw new BusinessException("订单状态不正确，当前状态：" + order.getStatus());
        }
        
        // 记录模拟支付
        MockPaymentRecord record = new MockPaymentRecord();
        record.setOrderNo(orderNo);
        record.setPaymentMethod(order.getPaymentMethod());
        record.setAmount(order.getAmount());
        record.setMockResult(success ? 1 : 0);
        record.setMockMessage(success ? "模拟支付成功" : "模拟支付失败");
        record.setCreateTime(new Date());
        mockPaymentRecordMapper.insert(record);
        
        if (success) {
            // 支付成功
            order.setStatus(PaymentOrderStatus.SUCCESS.getCode());
            order.setCallbackTime(new Date());
            order.setUpdateTime(new Date());
            paymentOrderMapper.updateById(order);
            
            // 处理业务逻辑
            handlePaymentSuccess(order);
            
            logger.info("模拟支付成功：订单号 {}", orderNo);
        } else {
            // 支付失败
            order.setStatus(PaymentOrderStatus.FAILED.getCode());
            order.setUpdateTime(new Date());
            paymentOrderMapper.updateById(order);
            
            logger.info("模拟支付失败：订单号 {}", orderNo);
        }
        
        logger.info("========== 支付回调方法即将返回，事务即将提交 ==========");
        return true;
    }
    
    /**
     * 处理支付成功后的业务逻辑
     */
    private void handlePaymentSuccess(PaymentOrder order) {
        if (OrderType.RECHARGE.getCode().equals(order.getOrderType())) {
            // 充值成功，增加钱包余额
            walletService.recharge(order.getUserId(), order.getAmount(), order.getPaymentMethod());
            logger.info("充值成功：用户 {}，金额 {}", order.getUserId(), order.getAmount());
            
        } else if (OrderType.PROPERTY_FEE.getCode().equals(order.getOrderType())) {
            // 物业费支付成功的处理
            logger.info("=== 物业费支付成功处理开始 ===");
            logger.info("订单号: {}", order.getOrderNo());
            logger.info("支付方式: {}", order.getPaymentMethod());
            logger.info("用户ID: {}", order.getUserId());
            logger.info("账单ID: {}", order.getRelatedId());
            logger.info("金额: {}", order.getAmount());
            
            // relatedId 存储的是账单ID (billId)
            if (order.getRelatedId() == null) {
                logger.error("物业费订单缺少关联账单ID：订单号 {}", order.getOrderNo());
                throw new BusinessException("物业费订单缺少关联账单ID");
            }
            
            try {
                // 如果是钱包支付，扣除钱包余额
                String paymentMethod = order.getPaymentMethod();
                logger.info("检查支付方式: paymentMethod=[{}], WALLET=[{}], equals={}", 
                    paymentMethod, PaymentMethod.WALLET.name(), 
                    PaymentMethod.WALLET.name().equals(paymentMethod));
                    
                if ("WALLET".equals(paymentMethod) || PaymentMethod.WALLET.name().equals(paymentMethod)) {
                    logger.info("===== 开始扣除钱包余额 =====");
                    logger.info("用户ID: {}, 金额: {}, 订单号: {}", order.getUserId(), order.getAmount(), order.getOrderNo());
                    
                    WalletTransaction transaction = walletService.payment(
                        order.getUserId(),
                        order.getAmount(),
                        order.getOrderNo(),
                        "缴纳物业费"
                    );
                    
                    logger.info("===== 钱包扣款成功 =====");
                    logger.info("交易ID: {}, 交易号: {}", transaction.getTransactionId(), transaction.getTransactionNo());
                } else {
                    logger.info("非钱包支付，跳过钱包扣款，当前支付方式: {}", paymentMethod);
                }
                
                // 调用物业费服务处理第三方支付成功（更新账单状态）
                logger.info("开始更新物业费账单状态...");
                PropertyFeePayment payment = propertyFeeService.handleThirdPartyPaymentSuccess(
                    order.getUserId(),
                    order.getRelatedId(), // billId
                    order.getAmount(),
                    order.getPaymentMethod(),
                    order.getOrderNo()
                );
                logger.info("物业费账单更新成功，缴费记录ID: {}", payment.getPaymentId());
                
                logger.info("=== 物业费支付处理完成：用户 {}，账单 {}，金额 {} ===", 
                    order.getUserId(), order.getRelatedId(), order.getAmount());
                    
            } catch (Exception e) {
                logger.error("物业费支付处理失败：订单号 {}，错误详情：", order.getOrderNo(), e);
                throw new BusinessException("物业费支付处理失败：" + e.getMessage());
            }
        }
    }
    
    @Override
    public PaymentOrder queryOrderStatus(String orderNo) {
        LambdaQueryWrapper<PaymentOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentOrder::getOrderNo, orderNo);
        PaymentOrder order = paymentOrderMapper.selectOne(wrapper);
        
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        return order;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(String orderNo, Long userId) {
        LambdaQueryWrapper<PaymentOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentOrder::getOrderNo, orderNo)
                .eq(PaymentOrder::getUserId, userId);
        PaymentOrder order = paymentOrderMapper.selectOne(wrapper);
        
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        if (order.getStatus().equals(PaymentOrderStatus.SUCCESS.getCode())) {
            throw new BusinessException("订单已支付，无法取消");
        }
        
        if (order.getStatus().equals(PaymentOrderStatus.CANCELLED.getCode())) {
            throw new BusinessException("订单已取消");
        }
        
        order.setStatus(PaymentOrderStatus.CANCELLED.getCode());
        order.setUpdateTime(new Date());
        paymentOrderMapper.updateById(order);
        
        logger.info("取消订单：订单号 {}", orderNo);
        
        return true;
    }
}
