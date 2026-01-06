package org.sc.smartcommunitybackend.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 交易流水号生成器
 */
public class TransactionNoGenerator {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    
    /**
     * 生成钱包交易流水号
     * 格式：WTR + 时间戳 + 随机数
     * 例如：WTR20260105134500A1B2C3D4
     */
    public static String generateWalletTransactionNo() {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "WTR" + timestamp + random;
    }
    
    /**
     * 生成物业费账单编号
     * 格式：BILL + 时间戳 + 随机数
     * 例如：BILL20260105134500A1B2C3D4
     */
    public static String generateBillNo() {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "BILL" + timestamp + random;
    }
    
    /**
     * 生成缴费流水号
     * 格式：PAY + 时间戳 + 随机数
     * 例如：PAY20260105134500A1B2C3D4
     */
    public static String generatePaymentNo() {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "PAY" + timestamp + random;
    }
}
