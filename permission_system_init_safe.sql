-- 权限管理系统数据库表结构（安全版本）
-- 执行此脚本前请确保数据库已创建
-- 此版本会检查列是否存在，避免重复执行时报错

-- 1. 安全地为 sys_role 表添加缺失字段
DELIMITER $$

-- 添加 role_code 字段
DROP PROCEDURE IF EXISTS add_role_code_column$$
CREATE PROCEDURE add_role_code_column()
BEGIN
    IF NOT EXISTS (
        SELECT * FROM information_schema.COLUMNS 
        WHERE TABLE_SCHEMA = DATABASE() 
        AND TABLE_NAME = 'sys_role' 
        AND COLUMN_NAME = 'role_code'
    ) THEN
        ALTER TABLE sys_role ADD COLUMN role_code VARCHAR(50) COMMENT '角色编码' AFTER role_name;
    END IF;
END$$

-- 添加 description 字段
DROP PROCEDURE IF EXISTS add_role_description_column$$
CREATE PROCEDURE add_role_description_column()
BEGIN
    IF NOT EXISTS (
        SELECT * FROM information_schema.COLUMNS 
        WHERE TABLE_SCHEMA = DATABASE() 
        AND TABLE_NAME = 'sys_role' 
        AND COLUMN_NAME = 'description'
    ) THEN
        ALTER TABLE sys_role ADD COLUMN description VARCHAR(500) COMMENT '角色描述' AFTER role_code;
    END IF;
END$$

-- 添加 status 字段
DROP PROCEDURE IF EXISTS add_role_status_column$$
CREATE PROCEDURE add_role_status_column()
BEGIN
    IF NOT EXISTS (
        SELECT * FROM information_schema.COLUMNS 
        WHERE TABLE_SCHEMA = DATABASE() 
        AND TABLE_NAME = 'sys_role' 
        AND COLUMN_NAME = 'status'
    ) THEN
        ALTER TABLE sys_role ADD COLUMN status INT DEFAULT 1 COMMENT '状态 0-禁用 1-启用' AFTER description;
    END IF;
END$$

DELIMITER ;

-- 执行存储过程
CALL add_role_code_column();
CALL add_role_description_column();
CALL add_role_status_column();

-- 删除存储过程
DROP PROCEDURE IF EXISTS add_role_code_column;
DROP PROCEDURE IF EXISTS add_role_description_column;
DROP PROCEDURE IF EXISTS add_role_status_column;

-- 2. 创建权限表
CREATE TABLE IF NOT EXISTS sys_permission (
    permission_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '权限ID',
    permission_name VARCHAR(100) NOT NULL COMMENT '权限名称',
    permission_code VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码',
    resource_type VARCHAR(20) DEFAULT 'api' COMMENT '资源类型（menu-菜单 button-按钮 api-接口）',
    description VARCHAR(500) COMMENT '权限描述',
    status INT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_permission_code (permission_code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统权限表';

-- 3. 创建角色权限关联表
CREATE TABLE IF NOT EXISTS sys_role_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    UNIQUE KEY uk_role_permission (role_id, permission_id),
    INDEX idx_role_id (role_id),
    INDEX idx_permission_id (permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 4. 初始化默认角色
INSERT INTO sys_role (role_name, role_code, description, status, create_time, update_time) VALUES
('超级管理员', 'ROLE_SUPER_ADMIN', '拥有系统所有权限', 1, NOW(), NOW()),
('社区管理员', 'ROLE_COMMUNITY_ADMIN', '社区管理相关权限', 1, NOW(), NOW()),
('商户管理员', 'ROLE_MERCHANT_ADMIN', '商城管理相关权限', 1, NOW(), NOW()),
('普通用户', 'ROLE_USER', '基础用户权限', 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE update_time = NOW();

-- 5. 初始化默认权限
INSERT INTO sys_permission (permission_name, permission_code, resource_type, description, status) VALUES
-- 用户管理权限
('查看用户', 'user:view', 'api', '查看用户信息', 1),
('创建用户', 'user:create', 'api', '创建新用户', 1),
('更新用户', 'user:update', 'api', '更新用户信息', 1),
('删除用户', 'user:delete', 'api', '删除用户', 1),
('分配角色', 'user:assign-role', 'api', '为用户分配角色', 1),
('查看用户权限', 'user:view-permission', 'api', '查看用户的权限信息', 1),

-- 角色管理权限
('查看角色', 'role:view', 'api', '查看角色信息', 1),
('创建角色', 'role:create', 'api', '创建新角色', 1),
('更新角色', 'role:update', 'api', '更新角色信息', 1),
('删除角色', 'role:delete', 'api', '删除角色', 1),
('分配权限', 'role:assign-permission', 'api', '为角色分配权限', 1),

-- 权限管理权限
('查看权限', 'permission:view', 'api', '查看权限信息', 1),
('创建权限', 'permission:create', 'api', '创建新权限', 1),
('更新权限', 'permission:update', 'api', '更新权限信息', 1),
('删除权限', 'permission:delete', 'api', '删除权限', 1),

-- 投诉管理权限
('查看投诉', 'complaint:view', 'api', '查看投诉信息', 1),
('审核投诉', 'complaint:audit', 'api', '审核投诉', 1),
('处理投诉', 'complaint:handle', 'api', '处理投诉', 1),

-- 报修管理权限
('查看报修', 'repair:view', 'api', '查看报修信息', 1),
('审核报修', 'repair:audit', 'api', '审核报修', 1),
('处理报修', 'repair:handle', 'api', '处理报修', 1),

-- 停车管理权限
('查看停车', 'parking:view', 'api', '查看停车信息', 1),
('审批停车', 'parking:approve', 'api', '审批停车申请', 1),

-- 公告管理权限
('查看公告', 'announcement:view', 'api', '查看公告', 1),
('创建公告', 'announcement:create', 'api', '创建公告', 1),
('更新公告', 'announcement:update', 'api', '更新公告', 1),
('删除公告', 'announcement:delete', 'api', '删除公告', 1),

-- 访客管理权限
('查看访客', 'visitor:view', 'api', '查看访客信息', 1),
('审核访客', 'visitor:audit', 'api', '审核访客', 1),

-- 商城管理权限
('查看商品', 'mall:view', 'api', '查看商品信息', 1),
('管理商品', 'mall:manage', 'api', '管理商品（增删改）', 1),
('查看订单', 'order:view', 'api', '查看订单信息', 1),
('处理订单', 'order:handle', 'api', '处理订单', 1)
ON DUPLICATE KEY UPDATE update_time = NOW();

-- 6. 为超级管理员分配所有权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 
    (SELECT role_id FROM sys_role WHERE role_code = 'ROLE_SUPER_ADMIN'),
    permission_id
FROM sys_permission
WHERE status = 1
ON DUPLICATE KEY UPDATE id = id;

-- 7. 为社区管理员分配社区管理相关权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 
    (SELECT role_id FROM sys_role WHERE role_code = 'ROLE_COMMUNITY_ADMIN'),
    permission_id
FROM sys_permission
WHERE permission_code IN (
    'user:view', 'complaint:view', 'complaint:audit', 'complaint:handle',
    'repair:view', 'repair:audit', 'repair:handle',
    'parking:view', 'parking:approve',
    'announcement:view', 'announcement:create', 'announcement:update', 'announcement:delete',
    'visitor:view', 'visitor:audit'
)
ON DUPLICATE KEY UPDATE id = id;

-- 8. 为商户管理员分配商城管理相关权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 
    (SELECT role_id FROM sys_role WHERE role_code = 'ROLE_MERCHANT_ADMIN'),
    permission_id
FROM sys_permission
WHERE permission_code IN (
    'mall:view', 'mall:manage', 'order:view', 'order:handle'
)
ON DUPLICATE KEY UPDATE id = id;

-- 9. 为普通用户分配基础权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 
    (SELECT role_id FROM sys_role WHERE role_code = 'ROLE_USER'),
    permission_id
FROM sys_permission
WHERE permission_code IN (
    'user:view', 'complaint:view', 'repair:view', 'parking:view',
    'announcement:view', 'visitor:view', 'mall:view', 'order:view'
)
ON DUPLICATE KEY UPDATE id = id;

-- 10. 查看初始化结果
SELECT '角色列表：' AS '';
SELECT role_id, role_name, role_code, description, status FROM sys_role;

SELECT '权限列表：' AS '';
SELECT permission_id, permission_name, permission_code, resource_type FROM sys_permission LIMIT 10;

SELECT '角色权限关联统计：' AS '';
SELECT r.role_name, COUNT(rp.permission_id) AS permission_count
FROM sys_role r
LEFT JOIN sys_role_permission rp ON r.role_id = rp.role_id
GROUP BY r.role_id, r.role_name;
