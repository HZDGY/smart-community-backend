package org.sc.smartcommunitybackend.service;

import org.sc.smartcommunitybackend.domain.PropertyFeeBill;
import org.sc.smartcommunitybackend.domain.PropertyFeePayment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 物业费服务接口
 */
public interface PropertyFeeService {
    
    /**
     * 生成物业费账单
     * 
     * @param userId 用户ID
     * @param billingPeriod 账期
     * @param propertyFee 物业费
     * @param waterFee 水费
     * @param electricityFee 电费
     * @param gasFee 燃气费
     * @param parkingFee 停车费
     * @param otherFee 其他费用
     * @param dueDate 缴费截止日期
     * @return 账单
     */
    PropertyFeeBill generateBill(Long userId, String billingPeriod, 
                                 BigDecimal propertyFee, BigDecimal waterFee, 
                                 BigDecimal electricityFee, BigDecimal gasFee,
                                 BigDecimal parkingFee, BigDecimal otherFee, 
                                 Date dueDate);
    
    /**
     * 查询用户的物业费账单
     * 
     * @param userId 用户ID
     * @param status 状态（可选）
     * @param page 页码
     * @param size 每页数量
     * @return 账单列表
     */
    List<PropertyFeeBill> getUserBills(Long userId, Integer status, Integer page, Integer size);
    
    /**
     * 获取账单详情
     * 
     * @param billId 账单ID
     * @return 账单详情
     */
    PropertyFeeBill getBillDetail(Long billId);
    
    /**
     * 缴纳物业费
     * 
     * @param userId 用户ID
     * @param billId 账单ID
     * @param amount 缴费金额
     * @param paymentMethod 支付方式
     * @return 缴费记录
     */
    PropertyFeePayment payBill(Long userId, Long billId, BigDecimal amount, String paymentMethod);
    
    /**
     * 查询缴费记录
     * 
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页数量
     * @return 缴费记录列表
     */
    List<PropertyFeePayment> getPaymentRecords(Long userId, Integer page, Integer size);
    
    /**
     * 查询账单总数
     * 
     * @param userId 用户ID
     * @param status 状态（可选）
     * @return 总数
     */
    Long getBillCount(Long userId, Integer status);
    
    /**
     * 处理第三方支付成功后的账单更新（支付宝/微信支付回调使用）
     * 
     * @param userId 用户ID
     * @param billId 账单ID
     * @param amount 缴费金额
     * @param paymentMethod 支付方式
     * @param orderNo 支付订单号
     * @return 缴费记录
     */
    PropertyFeePayment handleThirdPartyPaymentSuccess(Long userId, Long billId, BigDecimal amount, 
                                                     String paymentMethod, String orderNo);
}
