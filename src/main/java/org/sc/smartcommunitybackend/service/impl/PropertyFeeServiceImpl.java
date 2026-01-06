package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.sc.smartcommunitybackend.domain.PropertyFeeBill;
import org.sc.smartcommunitybackend.domain.PropertyFeePayment;
import org.sc.smartcommunitybackend.domain.WalletTransaction;
import org.sc.smartcommunitybackend.enums.BillStatus;
import org.sc.smartcommunitybackend.enums.PaymentMethod;
import org.sc.smartcommunitybackend.exception.BusinessException;
import org.sc.smartcommunitybackend.mapper.PropertyFeeBillMapper;
import org.sc.smartcommunitybackend.mapper.PropertyFeePaymentMapper;
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
import java.util.List;

/**
 * 物业费服务实现类
 */
@Service
public class PropertyFeeServiceImpl implements PropertyFeeService {
    
    private static final Logger logger = LoggerFactory.getLogger(PropertyFeeServiceImpl.class);
    
    @Autowired
    private PropertyFeeBillMapper billMapper;
    
    @Autowired
    private PropertyFeePaymentMapper paymentMapper;
    
    @Autowired
    private WalletService walletService;
    
    @Override
    @Transactional
    public PropertyFeeBill generateBill(Long userId, String billingPeriod,
                                       BigDecimal propertyFee, BigDecimal waterFee,
                                       BigDecimal electricityFee, BigDecimal gasFee,
                                       BigDecimal parkingFee, BigDecimal otherFee,
                                       Date dueDate) {
        // 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (propertyFee != null) totalAmount = totalAmount.add(propertyFee);
        if (waterFee != null) totalAmount = totalAmount.add(waterFee);
        if (electricityFee != null) totalAmount = totalAmount.add(electricityFee);
        if (gasFee != null) totalAmount = totalAmount.add(gasFee);
        if (parkingFee != null) totalAmount = totalAmount.add(parkingFee);
        if (otherFee != null) totalAmount = totalAmount.add(otherFee);
        
        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("账单总金额必须大于0");
        }
        
        // 创建账单
        PropertyFeeBill bill = new PropertyFeeBill();
        bill.setBillNo(TransactionNoGenerator.generateBillNo());
        bill.setUserId(userId);
        bill.setBillingPeriod(billingPeriod);
        bill.setPropertyFee(propertyFee != null ? propertyFee : BigDecimal.ZERO);
        bill.setWaterFee(waterFee != null ? waterFee : BigDecimal.ZERO);
        bill.setElectricityFee(electricityFee != null ? electricityFee : BigDecimal.ZERO);
        bill.setGasFee(gasFee != null ? gasFee : BigDecimal.ZERO);
        bill.setParkingFee(parkingFee != null ? parkingFee : BigDecimal.ZERO);
        bill.setOtherFee(otherFee != null ? otherFee : BigDecimal.ZERO);
        bill.setTotalAmount(totalAmount);
        bill.setPaidAmount(BigDecimal.ZERO);
        bill.setStatus(BillStatus.UNPAID.getCode());
        bill.setDueDate(dueDate);
        bill.setCreateTime(new Date());
        bill.setUpdateTime(new Date());
        
        billMapper.insert(bill);
        
        logger.info("生成物业费账单：用户 {}，账期 {}，总金额 {}", userId, billingPeriod, totalAmount);
        
        return bill;
    }
    
    @Override
    public List<PropertyFeeBill> getUserBills(Long userId, Integer status, Integer page, Integer size) {
        Page<PropertyFeeBill> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<PropertyFeeBill> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PropertyFeeBill::getUserId, userId);
        
        if (status != null) {
            wrapper.eq(PropertyFeeBill::getStatus, status);
        }
        
        wrapper.orderByDesc(PropertyFeeBill::getCreateTime);
        
        Page<PropertyFeeBill> result = billMapper.selectPage(pageParam, wrapper);
        return result.getRecords();
    }
    
    @Override
    public PropertyFeeBill getBillDetail(Long billId) {
        PropertyFeeBill bill = billMapper.selectById(billId);
        if (bill == null) {
            throw new BusinessException("账单不存在");
        }
        return bill;
    }
    
    @Override
    @Transactional
    public PropertyFeePayment payBill(Long userId, Long billId, BigDecimal amount, String paymentMethod) {
        // 查询账单
        PropertyFeeBill bill = billMapper.selectById(billId);
        if (bill == null) {
            throw new BusinessException("账单不存在");
        }
        
        // 验证账单所属用户
        if (!bill.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此账单");
        }
        
        // 验证账单状态
        if (bill.getStatus().equals(BillStatus.PAID.getCode())) {
            throw new BusinessException("账单已缴清");
        }
        
        // 验证缴费金额
        BigDecimal remainingAmount = bill.getTotalAmount().subtract(bill.getPaidAmount());
        if (amount.compareTo(remainingAmount) > 0) {
            throw new BusinessException("缴费金额超过应缴金额");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("缴费金额必须大于0");
        }
        
        WalletTransaction walletTransaction = null;
        
        // 钱包支付 - 直接扣款
        if (PaymentMethod.WALLET.name().equals(paymentMethod)) {
            // 使用钱包支付
            walletTransaction = walletService.payment(
                userId, 
                amount, 
                bill.getBillNo(), 
                "缴纳物业费 - " + bill.getBillingPeriod()
            );
        } else {
            // 支付宝或微信支付 - 需要创建支付订单
            // 注意：这里只是创建订单，实际支付成功后需要通过回调更新账单状态
            throw new BusinessException("请使用支付接口创建支付订单，订单类型为PROPERTY_FEE");
        }
        
        // 更新账单（仅钱包支付时）
        BigDecimal newPaidAmount = bill.getPaidAmount().add(amount);
        bill.setPaidAmount(newPaidAmount);
        
        if (newPaidAmount.compareTo(bill.getTotalAmount()) >= 0) {
            // 已缴清
            bill.setStatus(BillStatus.PAID.getCode());
            bill.setPaidTime(new Date());
        } else {
            // 部分缴纳
            bill.setStatus(BillStatus.PARTIAL.getCode());
        }
        
        bill.setUpdateTime(new Date());
        billMapper.updateById(bill);
        
        // 创建缴费记录
        PropertyFeePayment payment = new PropertyFeePayment();
        payment.setPaymentNo(TransactionNoGenerator.generatePaymentNo());
        payment.setBillId(billId);
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        if (walletTransaction != null) {
            payment.setTransactionId(walletTransaction.getTransactionId());
        }
        payment.setStatus(1); // 成功
        payment.setCreateTime(new Date());
        
        paymentMapper.insert(payment);
        
        logger.info("缴纳物业费成功：用户 {}，账单 {}，金额 {}，支付方式 {}", 
                userId, billId, amount, paymentMethod);
        
        return payment;
    }
    
    @Override
    public List<PropertyFeePayment> getPaymentRecords(Long userId, Integer page, Integer size) {
        Page<PropertyFeePayment> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<PropertyFeePayment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PropertyFeePayment::getUserId, userId);
        wrapper.orderByDesc(PropertyFeePayment::getCreateTime);
        
        Page<PropertyFeePayment> result = paymentMapper.selectPage(pageParam, wrapper);
        return result.getRecords();
    }
    
    @Override
    public Long getBillCount(Long userId, Integer status) {
        LambdaQueryWrapper<PropertyFeeBill> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PropertyFeeBill::getUserId, userId);
        
        if (status != null) {
            wrapper.eq(PropertyFeeBill::getStatus, status);
        }
        
        return billMapper.selectCount(wrapper);
    }
}
