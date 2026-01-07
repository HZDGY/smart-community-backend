-- 订单过期管理 SQL 脚本

-- 1. 查询所有过期的待支付订单
SELECT 
    order_id,
    order_no,
    user_id,
    order_type,
    amount,
    status,
    create_time,
    expire_time,
    TIMESTAMPDIFF(MINUTE, create_time, expire_time) as expire_minutes,
    TIMESTAMPDIFF(MINUTE, expire_time, NOW()) as expired_minutes_ago
FROM payment_order
WHERE status = 0  -- 待支付
  AND expire_time IS NOT NULL
  AND expire_time < NOW()
ORDER BY create_time DESC;

-- 2. 统计过期订单数量
SELECT 
    COUNT(*) as total_expired_orders,
    SUM(amount) as total_expired_amount
FROM payment_order
WHERE status = 0
  AND expire_time IS NOT NULL
  AND expire_time < NOW();

-- 3. 手动删除过期订单（谨慎使用）
-- DELETE FROM payment_order
-- WHERE status = 0
--   AND expire_time IS NOT NULL
--   AND expire_time < NOW();

-- 4. 手动取消过期订单（更新状态为已取消）
-- UPDATE payment_order
-- SET status = 5,  -- 5-已取消
--     update_time = NOW()
-- WHERE status = 0
--   AND expire_time IS NOT NULL
--   AND expire_time < NOW();

-- 5. 查询即将过期的订单（未来10分钟内）
SELECT 
    order_id,
    order_no,
    user_id,
    order_type,
    amount,
    status,
    create_time,
    expire_time,
    TIMESTAMPDIFF(MINUTE, NOW(), expire_time) as minutes_until_expire
FROM payment_order
WHERE status = 0
  AND expire_time IS NOT NULL
  AND expire_time > NOW()
  AND expire_time < DATE_ADD(NOW(), INTERVAL 10 MINUTE)
ORDER BY expire_time ASC;

-- 6. 查询订单状态分布
SELECT 
    status,
    CASE status
        WHEN 0 THEN '待支付'
        WHEN 1 THEN '支付中'
        WHEN 2 THEN '支付成功'
        WHEN 3 THEN '已完成'
        WHEN 4 THEN '支付失败'
        WHEN 5 THEN '已取消'
        WHEN 6 THEN '已退款'
        ELSE '未知'
    END as status_desc,
    COUNT(*) as order_count,
    SUM(amount) as total_amount
FROM payment_order
GROUP BY status
ORDER BY status;

-- 7. 查询今天创建的订单及其过期情况
SELECT 
    order_id,
    order_no,
    user_id,
    order_type,
    amount,
    status,
    CASE status
        WHEN 0 THEN '待支付'
        WHEN 1 THEN '支付中'
        WHEN 2 THEN '支付成功'
        WHEN 3 THEN '已完成'
        WHEN 4 THEN '支付失败'
        WHEN 5 THEN '已取消'
        WHEN 6 THEN '已退款'
        ELSE '未知'
    END as status_desc,
    create_time,
    expire_time,
    CASE 
        WHEN expire_time IS NULL THEN '无过期时间'
        WHEN expire_time < NOW() THEN '已过期'
        ELSE CONCAT('剩余', TIMESTAMPDIFF(MINUTE, NOW(), expire_time), '分钟')
    END as expire_status
FROM payment_order
WHERE DATE(create_time) = CURDATE()
ORDER BY create_time DESC;

-- 8. 查询商品订单及其商品详情
SELECT 
    po.order_id,
    po.order_no,
    po.user_id,
    po.amount,
    po.status,
    po.create_time,
    po.expire_time,
    op.product_name,
    op.quantity,
    op.price,
    op.subtotal
FROM payment_order po
LEFT JOIN order_product op ON po.order_id = op.order_id
WHERE po.order_type = 'PRODUCT'
  AND po.status = 0
  AND po.expire_time IS NOT NULL
ORDER BY po.create_time DESC;
