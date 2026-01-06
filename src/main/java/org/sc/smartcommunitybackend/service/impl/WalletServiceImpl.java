package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.sc.smartcommunitybackend.domain.UserWallet;
import org.sc.smartcommunitybackend.domain.WalletTransaction;
import org.sc.smartcommunitybackend.enums.TransactionType;
import org.sc.smartcommunitybackend.exception.BusinessException;
import org.sc.smartcommunitybackend.mapper.UserWalletMapper;
import org.sc.smartcommunitybackend.mapper.WalletTransactionMapper;
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
 * 钱包服务实现类
 */
@Service
public class WalletServiceImpl implements WalletService {
    
    private static final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);
    
    @Autowired
    private UserWalletMapper walletMapper;
    
    @Autowired
    private WalletTransactionMapper transactionMapper;
    
    @Override
    public UserWallet getOrCreateWallet(Long userId) {
        LambdaQueryWrapper<UserWallet> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserWallet::getUserId, userId);
        UserWallet wallet = walletMapper.selectOne(wrapper);
        
        if (wallet == null) {
            // 创建新钱包
            wallet = new UserWallet();
            wallet.setUserId(userId);
            wallet.setBalance(BigDecimal.ZERO);
            wallet.setFrozenAmount(BigDecimal.ZERO);
            wallet.setTotalRecharge(BigDecimal.ZERO);
            wallet.setTotalExpense(BigDecimal.ZERO);
            wallet.setStatus(1);
            wallet.setCreateTime(new Date());
            wallet.setUpdateTime(new Date());
            walletMapper.insert(wallet);
            logger.info("为用户 {} 创建钱包，钱包ID: {}", userId, wallet.getWalletId());
        }
        
        return wallet;
    }
    
    @Override
    public UserWallet getWalletInfo(Long userId) {
        return getOrCreateWallet(userId);
    }
    
    @Override
    @Transactional
    public WalletTransaction recharge(Long userId, BigDecimal amount, String paymentMethod) {
        // 参数校验
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("充值金额必须大于0");
        }
        if (amount.compareTo(new BigDecimal("10000")) > 0) {
            throw new BusinessException("单次充值金额不能超过10000元");
        }
        
        // 获取钱包
        UserWallet wallet = getOrCreateWallet(userId);
        
        // 更新余额
        BigDecimal balanceBefore = wallet.getBalance();
        BigDecimal balanceAfter = balanceBefore.add(amount);
        
        wallet.setBalance(balanceAfter);
        BigDecimal totalRecharge = wallet.getTotalRecharge() != null ? wallet.getTotalRecharge() : BigDecimal.ZERO;
        wallet.setTotalRecharge(totalRecharge.add(amount));
        wallet.setUpdateTime(new Date());
        walletMapper.updateById(wallet);
        
        // 记录交易
        WalletTransaction transaction = new WalletTransaction();
        transaction.setUserId(userId);
        transaction.setTransactionNo(TransactionNoGenerator.generateWalletTransactionNo());
        transaction.setTransactionType(TransactionType.RECHARGE.name());
        transaction.setAmount(amount);
        transaction.setBalanceBefore(balanceBefore);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setDescription("钱包充值 - " + paymentMethod);
        transaction.setCreateTime(new Date());
        transactionMapper.insert(transaction);
        
        logger.info("用户 {} 充值成功，金额: {}，余额: {} -> {}", 
                userId, amount, balanceBefore, balanceAfter);
        
        return transaction;
    }
    
    @Override
    @Transactional
    public WalletTransaction transfer(Long fromUserId, Long toUserId, BigDecimal amount, String description) {
        // 参数校验
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("转账金额必须大于0");
        }
        if (fromUserId.equals(toUserId)) {
            throw new BusinessException("不能给自己转账");
        }
        
        // 锁定钱包（防止并发问题）
        UserWallet fromWallet = walletMapper.selectByUserIdForUpdate(fromUserId);
        UserWallet toWallet = walletMapper.selectByUserIdForUpdate(toUserId);
        
        if (fromWallet == null) {
            throw new BusinessException("转出方钱包不存在");
        }
        if (toWallet == null) {
            // 自动创建收款方钱包
            toWallet = getOrCreateWallet(toUserId);
            // 重新锁定
            toWallet = walletMapper.selectByUserIdForUpdate(toUserId);
        }
        
        // 检查余额
        if (fromWallet.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("余额不足");
        }
        
        // 扣减转出方余额
        BigDecimal fromBalanceBefore = fromWallet.getBalance();
        BigDecimal fromBalanceAfter = fromBalanceBefore.subtract(amount);
        fromWallet.setBalance(fromBalanceAfter);
        BigDecimal totalExpense = fromWallet.getTotalExpense() != null ? fromWallet.getTotalExpense() : BigDecimal.ZERO;
        fromWallet.setTotalExpense(totalExpense.add(amount));
        fromWallet.setUpdateTime(new Date());
        walletMapper.updateById(fromWallet);
        
        // 增加转入方余额
        BigDecimal toBalanceBefore = toWallet.getBalance();
        BigDecimal toBalanceAfter = toBalanceBefore.add(amount);
        toWallet.setBalance(toBalanceAfter);
        toWallet.setUpdateTime(new Date());
        walletMapper.updateById(toWallet);
        
        // 记录转出方交易
        WalletTransaction fromTransaction = new WalletTransaction();
        fromTransaction.setUserId(fromUserId);
        fromTransaction.setTransactionNo(TransactionNoGenerator.generateWalletTransactionNo());
        fromTransaction.setTransactionType(TransactionType.TRANSFER_OUT.name());
        fromTransaction.setAmount(amount);
        fromTransaction.setBalanceBefore(fromBalanceBefore);
        fromTransaction.setBalanceAfter(fromBalanceAfter);
        fromTransaction.setRelatedUserId(toUserId);
        fromTransaction.setDescription(description != null ? description : "转账");
        fromTransaction.setCreateTime(new Date());
        transactionMapper.insert(fromTransaction);
        
        // 记录转入方交易
        WalletTransaction toTransaction = new WalletTransaction();
        toTransaction.setUserId(toUserId);
        toTransaction.setTransactionNo(TransactionNoGenerator.generateWalletTransactionNo());
        toTransaction.setTransactionType(TransactionType.TRANSFER_IN.name());
        toTransaction.setAmount(amount);
        toTransaction.setBalanceBefore(toBalanceBefore);
        toTransaction.setBalanceAfter(toBalanceAfter);
        toTransaction.setRelatedUserId(fromUserId);
        toTransaction.setDescription(description != null ? description : "转账");
        toTransaction.setCreateTime(new Date());
        transactionMapper.insert(toTransaction);
        
        logger.info("转账成功：{} -> {}，金额: {}", fromUserId, toUserId, amount);
        
        return fromTransaction;
    }
    
    @Override
    @Transactional
    public WalletTransaction payment(Long userId, BigDecimal amount, String orderNo, String description) {
        // 参数校验
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("支付金额必须大于0");
        }
        
        // 锁定钱包
        UserWallet wallet = walletMapper.selectByUserIdForUpdate(userId);
        if (wallet == null) {
            throw new BusinessException("钱包不存在");
        }
        
        // 检查余额
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("余额不足");
        }
        
        // 扣减余额
        BigDecimal balanceBefore = wallet.getBalance();
        BigDecimal balanceAfter = balanceBefore.subtract(amount);
        wallet.setBalance(balanceAfter);
        BigDecimal totalExpense = wallet.getTotalExpense() != null ? wallet.getTotalExpense() : BigDecimal.ZERO;
        wallet.setTotalExpense(totalExpense.add(amount));
        wallet.setUpdateTime(new Date());
        walletMapper.updateById(wallet);
        
        // 记录交易
        WalletTransaction transaction = new WalletTransaction();
        transaction.setUserId(userId);
        transaction.setTransactionNo(TransactionNoGenerator.generateWalletTransactionNo());
        transaction.setTransactionType(TransactionType.PAYMENT.name());
        transaction.setAmount(amount);
        transaction.setBalanceBefore(balanceBefore);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setRelatedOrderNo(orderNo);
        transaction.setDescription(description != null ? description : "支付");
        transaction.setCreateTime(new Date());
        transactionMapper.insert(transaction);
        
        logger.info("用户 {} 支付成功，金额: {}，订单号: {}", userId, amount, orderNo);
        
        return transaction;
    }
    
    @Override
    public List<WalletTransaction> getTransactions(Long userId, String transactionType, Integer page, Integer size) {
        Page<WalletTransaction> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<WalletTransaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WalletTransaction::getUserId, userId);
        
        if (transactionType != null && !transactionType.equals("ALL")) {
            wrapper.eq(WalletTransaction::getTransactionType, transactionType);
        }
        
        wrapper.orderByDesc(WalletTransaction::getCreateTime);
        
        Page<WalletTransaction> result = transactionMapper.selectPage(pageParam, wrapper);
        return result.getRecords();
    }
    
    @Override
    public Long getTransactionCount(Long userId, String transactionType) {
        LambdaQueryWrapper<WalletTransaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WalletTransaction::getUserId, userId);
        
        if (transactionType != null && !transactionType.equals("ALL")) {
            wrapper.eq(WalletTransaction::getTransactionType, transactionType);
        }
        
        return transactionMapper.selectCount(wrapper);
    }
}
