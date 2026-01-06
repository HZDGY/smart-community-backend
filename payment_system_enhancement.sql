-- 支付系统增强脚本
-- 创建支付订单表

-- 支付订单表
CREATE TABLE IF NOT EXISTS `payment_order` (
    `order_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` VARCHAR(64) NOT NULL COMMENT '订单号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `order_type` VARCHAR(32) NOT NULL COMMENT '订单类型：RECHARGE-充值, PROPERTY_FEE-物业费',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '订单金额',
    `payment_method` VARCHAR(32) NOT NULL COMMENT '支付方式：ALIPAY-支付宝, WECHAT-微信, WALLET-钱包',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态：0-待支付, 1-支付中, 2-支付成功, 3-支付失败, 4-已取消',
    `related_id` BIGINT COMMENT '关联业务ID（如账单ID）',
    `third_party_order_no` VARCHAR(128) COMMENT '第三方订单号',
    `callback_time` DATETIME COMMENT '回调时间',
    `expire_time` DATETIME COMMENT '过期时间',
    `description` VARCHAR(255) COMMENT '订单描述',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`order_id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付订单表';

-- 模拟支付记录表（用于开发测试）
CREATE TABLE IF NOT EXISTS `mock_payment_record` (
    `record_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `order_no` VARCHAR(64) NOT NULL COMMENT '订单号',
    `payment_method` VARCHAR(32) NOT NULL COMMENT '支付方式',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '支付金额',
    `mock_result` TINYINT NOT NULL COMMENT '模拟结果：1-成功, 0-失败',
    `mock_message` VARCHAR(255) COMMENT '模拟消息',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`record_id`),
    KEY `idx_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模拟支付记录表';
