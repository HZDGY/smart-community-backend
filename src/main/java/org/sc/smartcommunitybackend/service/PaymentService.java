package org.sc.smartcommunitybackend.service;

import org.sc.smartcommunitybackend.domain.PaymentOrder;
import org.sc.smartcommunitybackend.dto.response.PaymentResponse;

import java.math.BigDecimal;

/**
 * 支付服务接口
 */
public interface PaymentService {
    
    /**
     * 创建支付订单
     *
     * @param userId 用户ID
     * @param orderType 订单类型
     * @param amount 金额
     * @param paymentMethod 支付方式
     * @param relatedId 关联业务ID
     * @param description 描述
     * @return 支付订单
     */
    PaymentOrder createPaymentOrder(Long userId, String orderType, BigDecimal amount, 
                                   String paymentMethod, Long relatedId, String description);
    
    /**
     * 发起支付
     *
     * @param orderNo 订单号
     * @return 支付响应
     */
    PaymentResponse initiatePayment(String orderNo);
    
    /**
     * 模拟支付回调（用于开发测试）
     *
     * @param orderNo 订单号
     * @param success 是否成功
     * @return 是否处理成功
     */
    boolean mockPaymentCallback(String orderNo, boolean success);
    
    /**
     * 查询订单状态
     *
     * @param orderNo 订单号
     * @return 支付订单
     */
    PaymentOrder queryOrderStatus(String orderNo);
    
    /**
     * 取消订单
     *
     * @param orderNo 订单号
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean cancelOrder(String orderNo, Long userId);
}
