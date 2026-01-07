-- 检查用户钱包余额和交易记录

-- 1. 查看用户钱包信息
SELECT 
    wallet_id,
    user_id,
    balance,
    frozen_amount,
    total_recharge,
    total_expense,
    status,
    create_time,
    update_time
FROM user_wallet
WHERE user_id = 13;

-- 2. 查看最近的钱包交易记录
SELECT 
    transaction_id,
    user_id,
    transaction_no,
    transaction_type,
    amount,
    balance_before,
    balance_after,
    related_order_no,
    description,
    create_time
FROM wallet_transaction
WHERE user_id = 13
ORDER BY create_time DESC
LIMIT 10;

-- 3. 查看该支付订单
SELECT 
    order_id,
    order_no,
    user_id,
    order_type,
    amount,
    payment_method,
    status,
    related_id,
    description,
    create_time,
    callback_time
FROM payment_order
WHERE order_no = 'ORDER202601061431424E351A64';

-- 4. 查看物业费账单状态
SELECT 
    bill_id,
    bill_no,
    user_id,
    billing_period,
    total_amount,
    paid_amount,
    status,
    paid_time,
    create_time
FROM property_fee_bill
WHERE bill_id = 7;

-- 5. 查看物业费缴费记录
SELECT 
    payment_id,
    payment_no,
    bill_id,
    user_id,
    amount,
    payment_method,
    transaction_id,
    status,
    create_time
FROM property_fee_payment
WHERE bill_id = 7
ORDER BY create_time DESC;

-- 预期结果说明：
-- user_wallet.balance 应该减少 700.50
-- user_wallet.total_expense 应该增加 700.50
-- wallet_transaction 应该有一条 PAYMENT 类型的记录，金额 700.50
-- payment_order.status 应该是 2 (支付成功)
-- property_fee_bill.status 应该是 1 (已缴)
-- property_fee_bill.paid_amount 应该增加 700.50
-- property_fee_payment 应该有一条新记录

