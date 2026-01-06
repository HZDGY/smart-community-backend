-- 为用户12和用户13生成物业费账单

-- 为用户12生成2026年1月物业费账单
INSERT INTO property_fee_bill (
    bill_no,
    user_id,
    billing_period,
    property_fee,
    water_fee,
    electricity_fee,
    gas_fee,
    parking_fee,
    other_fee,
    total_amount,
    paid_amount,
    status,
    due_date,
    create_time,
    update_time
) VALUES (
    CONCAT('BILL', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), SUBSTRING(MD5(RAND()), 1, 8)),
    12,
    '2026-01',
    300.00,   -- 物业费
    45.50,    -- 水费
    120.00,   -- 电费
    35.00,    -- 燃气费
    200.00,   -- 停车费
    0.00,     -- 其他费用
    700.50,   -- 总金额
    0.00,     -- 已缴金额
    0,        -- 状态：0-未缴
    '2026-02-10 23:59:59',  -- 缴费截止日期
    NOW(),
    NOW()
);

-- 为用户13生成2026年1月物业费账单
INSERT INTO property_fee_bill (
    bill_no,
    user_id,
    billing_period,
    property_fee,
    water_fee,
    electricity_fee,
    gas_fee,
    parking_fee,
    other_fee,
    total_amount,
    paid_amount,
    status,
    due_date,
    create_time,
    update_time
) VALUES (
    CONCAT('BILL', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), SUBSTRING(MD5(RAND()), 1, 8)),
    13,
    '2026-01',
    300.00,   -- 物业费
    52.30,    -- 水费
    135.80,   -- 电费
    40.00,    -- 燃气费
    200.00,   -- 停车费
    0.00,     -- 其他费用
    728.10,   -- 总金额
    0.00,     -- 已缴金额
    0,        -- 状态：0-未缴
    '2026-02-10 23:59:59',  -- 缴费截止日期
    NOW(),
    NOW()
);

-- 查看生成的账单
SELECT 
    bill_id,
    bill_no,
    user_id,
    billing_period,
    property_fee,
    water_fee,
    electricity_fee,
    gas_fee,
    parking_fee,
    total_amount,
    status,
    due_date
FROM property_fee_bill
WHERE user_id IN (12, 13)
ORDER BY user_id, create_time DESC;

SELECT '物业费账单生成成功' AS message;
