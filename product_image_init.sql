-- 商品图片表初始化脚本

-- 创建商品图片表
CREATE TABLE IF NOT EXISTS product_image (
    image_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '图片ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    image_url VARCHAR(500) NOT NULL COMMENT '图片URL',
    is_main TINYINT DEFAULT 0 COMMENT '是否主图 0-否 1-是',
    sort_order INT DEFAULT 0 COMMENT '排序顺序（数字越小越靠前）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_product_id (product_id),
    INDEX idx_sort_order (sort_order),
    INDEX idx_is_main (is_main)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品图片表';

-- 查看创建结果
SELECT '商品图片表创建完成' AS message;
DESCRIBE product_image;
