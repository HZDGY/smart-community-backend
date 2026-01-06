-- 更新现有 user_wallet 表，添加缺失字段

-- 添加 frozen_amount 字段
ALTER TABLE user_wallet ADD COLUMN frozen_amount DECIMAL(10, 2) DEFAULT 0.00 COMMENT '冻结金额' AFTER balance;

-- 添加 total_recharge 字段
ALTER TABLE user_wallet ADD COLUMN total_recharge DECIMAL(10, 2) DEFAULT 0.00 COMMENT '累计充值' AFTER frozen_amount;

-- 添加 total_expense 字段
ALTER TABLE user_wallet ADD COLUMN total_expense DECIMAL(10, 2) DEFAULT 0.00 COMMENT '累计支出' AFTER total_recharge;

-- 添加 status 字段
ALTER TABLE user_wallet ADD COLUMN status INT DEFAULT 1 COMMENT '状态 0-冻结 1-正常' AFTER total_expense;

SELECT '字段添加完成' AS message;

-- 查看表结构
DESCRIBE user_wallet;
