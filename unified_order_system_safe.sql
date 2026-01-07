-- ============================================
-- 统一订单系统 - 数据库设计（安全版本）
-- 可重复执行，自动检查字段是否存在
-- ============================================

-- 1. 订单商品关联表（中间表）
CREATE TABLE IF NOT EXISTS `order_product` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `order_id` BIGINT NOT NULL COMMENT '订单ID(关联payment_order.order_id)',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `product_name` VARCHAR(200) NOT NULL COMMENT '商品名称（冗余）',
    `product_image` VARCHAR(500) COMMENT '商品图片（冗余）',
    `store_id` BIGINT COMMENT '取货门店ID',
    `store_name` VARCHAR(200) COMMENT '门店名称（冗余）',
    `quantity` INT NOT NULL DEFAULT 1 COMMENT '商品数量',
    `price` DECIMAL(10,2) NOT NULL COMMENT '商品单价',
    `subtotal` DECIMAL(10,2) NOT NULL COMMENT '小计金额',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_order_id` (`order_id`),
    INDEX `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单商品关联表';

-- 2. 安全地添加 payment_order 表字段（使用存储过程）
DELIMITER //

-- 添加 store_id 字段
DROP PROCEDURE IF EXISTS add_store_id_column//
CREATE PROCEDURE add_store_id_column()
BEGIN
    IF NOT EXISTS (
        SELECT * FROM information_schema.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()
        AND TABLE_NAME = 'payment_order'
        AND COLUMN_NAME = 'store_id'
    ) THEN
        ALTER TABLE `payment_order` ADD COLUMN `store_id` BIGINT COMMENT '取货门店ID（商品订单）';
    END IF;
END//

-- 添加 pick_up_time 字段
DROP PROCEDURE IF EXISTS add_pick_up_time_column//
CREATE PROCEDURE add_pick_up_time_column()
BEGIN
    IF NOT EXISTS (
        SELECT * FROM information_schema.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()
        AND TABLE_NAME = 'payment_order'
        AND COLUMN_NAME = 'pick_up_time'
    ) THEN
        ALTER TABLE `payment_order` ADD COLUMN `pick_up_time` TIMESTAMP NULL COMMENT '取货时间';
    END IF;
END//

-- 添加 finish_time 字段
DROP PROCEDURE IF EXISTS add_finish_time_column//
CREATE PROCEDURE add_finish_time_column()
BEGIN
    IF NOT EXISTS (
        SELECT * FROM information_schema.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()
        AND TABLE_NAME = 'payment_order'
        AND COLUMN_NAME = 'finish_time'
    ) THEN
        ALTER TABLE `payment_order` ADD COLUMN `finish_time` TIMESTAMP NULL COMMENT '完成时间';
    END IF;
END//

DELIMITER ;

-- 执行存储过程
CALL add_store_id_column();
CALL add_pick_up_time_column();
CALL add_finish_time_column();

-- 删除存储过程
DROP PROCEDURE IF EXISTS add_store_id_column;
DROP PROCEDURE IF EXISTS add_pick_up_time_column;
DROP PROCEDURE IF EXISTS add_finish_time_column;

-- 3. 创建订单类型枚举说明
-- order_type 字段说明：
-- PRODUCT: 商品订单
-- RECHARGE: 钱包充值
-- PROPERTY_FEE: 物业费
-- PARKING_FEE: 停车费
-- SERVICE_FEE: 服务费
-- OTHER: 其他

-- 4. 订单状态说明 (payment_order.status)
-- 0: 待支付 (PENDING)
-- 1: 支付中 (PAYING)
-- 2: 支付成功/待取货 (SUCCESS/WAIT_PICKUP)
-- 3: 已取货/已完成 (COMPLETED)
-- 4: 支付失败 (FAILED)
-- 5: 已取消 (CANCELLED)
-- 6: 已退款 (REFUNDED)

-- 5. 为方便查询，创建视图（可选）
CREATE OR REPLACE VIEW `v_order_detail` AS
SELECT 
    po.order_id,
    po.order_no,
    po.user_id,
    po.order_type,
    po.amount,
    po.payment_method,
    po.status,
    po.store_id,
    po.description,
    po.create_time,
    po.update_time,
    po.expire_time,
    po.callback_time,
    po.pick_up_time,
    po.finish_time,
    COUNT(op.id) as product_count,
    SUM(op.quantity) as total_quantity
FROM payment_order po
LEFT JOIN order_product op ON po.order_id = op.order_id
GROUP BY po.order_id;

-- 6. 示例数据（可选）
-- 插入一个商品订单示例
-- INSERT INTO payment_order (order_no, user_id, order_type, amount, payment_method, status, store_id, description, create_time, update_time)
-- VALUES ('ORDER20260106000001', 1, 'PRODUCT', 99.99, 'ALIPAY', 0, 1, '商品订单', NOW(), NOW());
-- 
-- INSERT INTO order_product (order_id, product_id, product_name, quantity, price, subtotal)
-- VALUES (1, 1, '测试商品', 2, 49.99, 99.98);

-- 完成提示
SELECT '统一订单系统数据库初始化完成！' AS message;

