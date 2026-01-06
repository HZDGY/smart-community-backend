-- 用户钱包和物业费系统数据库表结构

-- 1. 创建用户钱包表
CREATE TABLE IF NOT EXISTS user_wallet (
    wallet_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '钱包ID',
    user_id BIGINT NOT NULL UNIQUE COMMENT '用户ID',
    balance DECIMAL(10, 2) DEFAULT 0.00 COMMENT '余额',
    frozen_amount DECIMAL(10, 2) DEFAULT 0.00 COMMENT '冻结金额',
    total_recharge DECIMAL(10, 2) DEFAULT 0.00 COMMENT '累计充值',
    total_expense DECIMAL(10, 2) DEFAULT 0.00 COMMENT '累计支出',
    status INT DEFAULT 1 COMMENT '状态 0-冻结 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户钱包表';

-- 2. 创建钱包交易记录表
CREATE TABLE IF NOT EXISTS wallet_transaction (
    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '交易ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    transaction_no VARCHAR(64) NOT NULL UNIQUE COMMENT '交易流水号',
    transaction_type VARCHAR(20) NOT NULL COMMENT '交易类型（RECHARGE-充值 TRANSFER_OUT-转出 TRANSFER_IN-转入 PAYMENT-支付 REFUND-退款）',
    amount DECIMAL(10, 2) NOT NULL COMMENT '交易金额',
    balance_before DECIMAL(10, 2) NOT NULL COMMENT '交易前余额',
    balance_after DECIMAL(10, 2) NOT NULL COMMENT '交易后余额',
    related_user_id BIGINT COMMENT '关联用户ID（转账时使用）',
    related_order_no VARCHAR(64) COMMENT '关联订单号',
    description VARCHAR(200) COMMENT '交易描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '交易时间',
    INDEX idx_user_id (user_id),
    INDEX idx_transaction_no (transaction_no),
    INDEX idx_transaction_type (transaction_type),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='钱包交易记录表';

-- 3. 创建物业费账单表
CREATE TABLE IF NOT EXISTS property_fee_bill (
    bill_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '账单ID',
    bill_no VARCHAR(64) NOT NULL UNIQUE COMMENT '账单编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    billing_period VARCHAR(20) NOT NULL COMMENT '账期（如：2026-01）',
    property_fee DECIMAL(10, 2) DEFAULT 0.00 COMMENT '物业费',
    water_fee DECIMAL(10, 2) DEFAULT 0.00 COMMENT '水费',
    electricity_fee DECIMAL(10, 2) DEFAULT 0.00 COMMENT '电费',
    gas_fee DECIMAL(10, 2) DEFAULT 0.00 COMMENT '燃气费',
    parking_fee DECIMAL(10, 2) DEFAULT 0.00 COMMENT '停车费',
    other_fee DECIMAL(10, 2) DEFAULT 0.00 COMMENT '其他费用',
    total_amount DECIMAL(10, 2) NOT NULL COMMENT '总金额',
    paid_amount DECIMAL(10, 2) DEFAULT 0.00 COMMENT '已缴金额',
    status INT DEFAULT 0 COMMENT '状态 0-未缴 1-已缴 2-部分缴纳',
    due_date DATE COMMENT '缴费截止日期',
    paid_time DATETIME COMMENT '缴费时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_bill_no (bill_no),
    INDEX idx_billing_period (billing_period),
    INDEX idx_status (status),
    INDEX idx_due_date (due_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物业费账单表';

-- 4. 创建物业费缴纳记录表
CREATE TABLE IF NOT EXISTS property_fee_payment (
    payment_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '缴费ID',
    payment_no VARCHAR(64) NOT NULL UNIQUE COMMENT '缴费流水号',
    bill_id BIGINT NOT NULL COMMENT '账单ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    amount DECIMAL(10, 2) NOT NULL COMMENT '缴费金额',
    payment_method VARCHAR(20) DEFAULT 'WALLET' COMMENT '支付方式（WALLET-钱包 ALIPAY-支付宝 WECHAT-微信）',
    transaction_id BIGINT COMMENT '关联钱包交易ID',
    status INT DEFAULT 1 COMMENT '状态 0-失败 1-成功 2-退款',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '缴费时间',
    INDEX idx_bill_id (bill_id),
    INDEX idx_user_id (user_id),
    INDEX idx_payment_no (payment_no),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物业费缴纳记录表';

-- 查看创建结果
SELECT '数据库表创建完成' AS message;
SHOW TABLES LIKE '%wallet%';
SHOW TABLES LIKE '%property_fee%';
