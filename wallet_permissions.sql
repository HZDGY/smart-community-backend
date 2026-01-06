-- 钱包和物业费系统权限配置

-- 添加钱包相关权限
INSERT INTO sys_permission (permission_name, permission_code, resource_type, description, status) VALUES
('查看钱包', 'wallet:view', 'api', '查看钱包信息和交易记录', 1),
('充值', 'wallet:recharge', 'api', '钱包充值', 1),
('转账', 'wallet:transfer', 'api', '钱包转账', 1),
('查看物业费', 'property-fee:view', 'api', '查看物业费账单', 1),
('缴纳物业费', 'property-fee:pay', 'api', '缴纳物业费', 1),
('管理物业费', 'property-fee:manage', 'api', '生成和管理物业费账单', 1)
ON DUPLICATE KEY UPDATE update_time = NOW();

-- 为普通用户分配钱包和物业费权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 
    (SELECT role_id FROM sys_role WHERE role_code = 'ROLE_USER'),
    permission_id
FROM sys_permission
WHERE permission_code IN (
    'wallet:view', 'wallet:recharge', 'wallet:transfer',
    'property-fee:view', 'property-fee:pay'
)
ON DUPLICATE KEY UPDATE id = id;

-- 为社区管理员分配物业费管理权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 
    (SELECT role_id FROM sys_role WHERE role_code = 'ROLE_COMMUNITY_ADMIN'),
    permission_id
FROM sys_permission
WHERE permission_code IN (
    'wallet:view', 'property-fee:view', 'property-fee:manage'
)
ON DUPLICATE KEY UPDATE id = id;

-- 为超级管理员分配所有新权限（如果之前没有）
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 
    (SELECT role_id FROM sys_role WHERE role_code = 'ROLE_SUPER_ADMIN'),
    permission_id
FROM sys_permission
WHERE permission_code IN (
    'wallet:view', 'wallet:recharge', 'wallet:transfer',
    'property-fee:view', 'property-fee:pay', 'property-fee:manage'
)
ON DUPLICATE KEY UPDATE id = id;

SELECT '权限配置完成' AS message;
