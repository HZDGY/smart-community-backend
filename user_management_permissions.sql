-- 用户管理权限配置

-- 添加用户管理相关权限
INSERT INTO sys_permission (permission_name, permission_code, resource_type, description, status) VALUES
('查看用户列表', 'user:list', 'api', '查看用户列表', 1),
('查看用户详情', 'user:view', 'api', '查看用户详情', 1),
('管理用户状态', 'user:status', 'api', '启用/冻结用户', 1),
('分配用户角色', 'user:role', 'api', '为用户分配角色', 1),
('编辑用户信息', 'user:edit', 'api', '编辑用户信息', 1),
('删除用户', 'user:delete', 'api', '删除用户', 1)
ON DUPLICATE KEY UPDATE update_time = NOW();

-- 为社区管理员分配用户管理权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 
    (SELECT role_id FROM sys_role WHERE role_code = 'ROLE_COMMUNITY_ADMIN'),
    permission_id
FROM sys_permission
WHERE permission_code IN (
    'user:list', 'user:view', 'user:status', 'user:role', 'user:edit', 'user:delete'
)
ON DUPLICATE KEY UPDATE id = id;

-- 为超级管理员分配所有用户管理权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 
    (SELECT role_id FROM sys_role WHERE role_code = 'ROLE_SUPER_ADMIN'),
    permission_id
FROM sys_permission
WHERE permission_code IN (
    'user:list', 'user:view', 'user:status', 'user:role', 'user:edit', 'user:delete'
)
ON DUPLICATE KEY UPDATE id = id;

SELECT '用户管理权限配置完成' AS message;
