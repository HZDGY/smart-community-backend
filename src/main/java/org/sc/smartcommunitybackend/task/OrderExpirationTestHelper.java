package org.sc.smartcommunitybackend.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.domain.PaymentOrder;
import org.sc.smartcommunitybackend.enums.OrderType;
import org.sc.smartcommunitybackend.enums.PaymentOrderStatus;
import org.sc.smartcommunitybackend.mapper.PaymentOrderMapper;
import org.sc.smartcommunitybackend.util.TransactionNoGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单过期功能测试工具类
 * 用于创建测试订单和验证过期功能
 */
@Slf4j
@Component
public class OrderExpirationTestHelper {

    @Autowired
    private PaymentOrderMapper paymentOrderMapper;

    /**
     * 创建一个即将过期的测试订单
     * @param userId 用户ID
     * @param expireMinutes 过期时间（分钟），可以设置为1或2分钟用于快速测试
     * @return 创建的订单
     */
    public PaymentOrder createTestOrder(Long userId, int expireMinutes) {
        PaymentOrder order = new PaymentOrder();
        order.setOrderNo(TransactionNoGenerator.generatePaymentOrderNo());
        order.setUserId(userId);
        order.setOrderType(OrderType.PRODUCT.getCode());
        order.setAmount(new BigDecimal("99.99"));
        order.setPaymentMethod("ALIPAY");
        order.setStatus(PaymentOrderStatus.PENDING.getCode());
        order.setDescription("测试订单 - 用于验证过期删除功能");
        
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + expireMinutes * 60 * 1000L);
        order.setExpireTime(expireTime);
        order.setCreateTime(now);
        order.setUpdateTime(now);
        
        paymentOrderMapper.insert(order);
        
        log.info("创建测试订单成功 - 订单号: {}, 订单ID: {}, 过期时间: {}, {}分钟后过期", 
                order.getOrderNo(), order.getOrderId(), expireTime, expireMinutes);
        
        return order;
    }

    /**
     * 创建一个已经过期的测试订单
     * @param userId 用户ID
     * @return 创建的订单
     */
    public PaymentOrder createExpiredTestOrder(Long userId) {
        PaymentOrder order = new PaymentOrder();
        order.setOrderNo(TransactionNoGenerator.generatePaymentOrderNo());
        order.setUserId(userId);
        order.setOrderType(OrderType.PRODUCT.getCode());
        order.setAmount(new BigDecimal("88.88"));
        order.setPaymentMethod("WECHAT");
        order.setStatus(PaymentOrderStatus.PENDING.getCode());
        order.setDescription("已过期测试订单");
        
        Date now = new Date();
        // 设置为5分钟前过期
        Date expireTime = new Date(now.getTime() - 5 * 60 * 1000L);
        order.setExpireTime(expireTime);
        order.setCreateTime(new Date(now.getTime() - 35 * 60 * 1000L)); // 35分钟前创建
        order.setUpdateTime(now);
        
        paymentOrderMapper.insert(order);
        
        log.info("创建已过期测试订单成功 - 订单号: {}, 订单ID: {}, 过期时间: {}", 
                order.getOrderNo(), order.getOrderId(), expireTime);
        
        return order;
    }

    /**
     * 查询所有过期的待支付订单
     * @return 过期订单列表
     */
    public java.util.List<PaymentOrder> findExpiredOrders() {
        LambdaQueryWrapper<PaymentOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PaymentOrder::getStatus, 0)
                .isNotNull(PaymentOrder::getExpireTime)
                .lt(PaymentOrder::getExpireTime, new Date());
        
        return paymentOrderMapper.selectList(queryWrapper);
    }

    /**
     * 打印过期订单信息
     */
    public void printExpiredOrders() {
        java.util.List<PaymentOrder> expiredOrders = findExpiredOrders();
        
        if (expiredOrders.isEmpty()) {
            log.info("当前没有过期订单");
            return;
        }
        
        log.info("========== 过期订单列表 ==========");
        log.info("共找到 {} 个过期订单", expiredOrders.size());
        
        for (PaymentOrder order : expiredOrders) {
            long expiredMinutes = (new Date().getTime() - order.getExpireTime().getTime()) / (60 * 1000);
            log.info("订单号: {}, 订单ID: {}, 金额: {}, 创建时间: {}, 过期时间: {}, 已过期{}分钟",
                    order.getOrderNo(), order.getOrderId(), order.getAmount(),
                    order.getCreateTime(), order.getExpireTime(), expiredMinutes);
        }
        
        log.info("==================================");
    }

    /**
     * 清理所有测试订单
     */
    public void cleanupTestOrders() {
        LambdaQueryWrapper<PaymentOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(PaymentOrder::getDescription, "测试订单");
        
        int count = paymentOrderMapper.delete(queryWrapper);
        log.info("清理测试订单完成，共删除 {} 个订单", count);
    }
}
