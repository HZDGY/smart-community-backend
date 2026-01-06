package org.sc.smartcommunitybackend.service;

import org.sc.smartcommunitybackend.domain.UserWallet;
import org.sc.smartcommunitybackend.domain.WalletTransaction;

import java.math.BigDecimal;
import java.util.List;

/**
 * 钱包服务接口
 */
public interface WalletService {
    
    /**
     * 获取或创建用户钱包
     * 
     * @param userId 用户ID
     * @return 用户钱包
     */
    UserWallet getOrCreateWallet(Long userId);
    
    /**
     * 获取钱包信息
     * 
     * @param userId 用户ID
     * @return 钱包信息
     */
    UserWallet getWalletInfo(Long userId);
    
    /**
     * 充值
     * 
     * @param userId 用户ID
     * @param amount 充值金额
     * @param paymentMethod 支付方式
     * @return 交易记录
     */
    WalletTransaction recharge(Long userId, BigDecimal amount, String paymentMethod);
    
    /**
     * 转账
     * 
     * @param fromUserId 转出用户ID
     * @param toUserId 转入用户ID
     * @param amount 转账金额
     * @param description 转账描述
     * @return 转出方的交易记录
     */
    WalletTransaction transfer(Long fromUserId, Long toUserId, BigDecimal amount, String description);
    
    /**
     * 支付（扣款）
     * 
     * @param userId 用户ID
     * @param amount 支付金额
     * @param orderNo 订单号
     * @param description 支付描述
     * @return 交易记录
     */
    WalletTransaction payment(Long userId, BigDecimal amount, String orderNo, String description);
    
    /**
     * 查询交易记录
     * 
     * @param userId 用户ID
     * @param transactionType 交易类型（可选）
     * @param page 页码
     * @param size 每页数量
     * @return 交易记录列表
     */
    List<WalletTransaction> getTransactions(Long userId, String transactionType, Integer page, Integer size);
    
    /**
     * 查询交易记录总数
     * 
     * @param userId 用户ID
     * @param transactionType 交易类型（可选）
     * @return 总数
     */
    Long getTransactionCount(Long userId, String transactionType);
}
