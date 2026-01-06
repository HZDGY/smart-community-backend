# 会员管理功能使用指南

## 功能概述

会员管理是后台管理功能，用于管理系统中的所有用户（会员）。管理员可以查看用户列表、查看用户详情、管理用户状态、分配角色等。

## 权限要求

所有接口都需要管理员权限，具体权限如下：

| 权限代码 | 权限名称 | 说明 |
|---------|---------|------|
| user:list | 查看用户列表 | 查询用户列表 |
| user:view | 查看用户详情 | 查看用户详细信息 |
| user:status | 管理用户状态 | 启用/冻结用户账号 |
| user:role | 分配用户角色 | 为用户分配角色 |
| user:edit | 编辑用户信息 | 更新用户基本信息 |
| user:delete | 删除用户 | 删除用户账号 |

## 快速开始

### 1. 配置权限

执行权限配置脚本：

```bash
mysql -u root -p your_database < user_management_permissions.sql
```

### 2. 重启项目

```bash
mvn spring-boot:run
```

### 3. 访问 API 文档

打开 `http://localhost:8080/doc.html` 查看用户管理接口。

## API 使用说明

### 1. 查询用户列表

```http
GET /admin/users?page=1&size=10&keyword=&status=&userType=
Authorization: Bearer {admin_token}
```

**请求参数**：
- `page`: 页码（默认1）
- `size`: 每页数量（默认10）
- `keyword`: 关键词（搜索用户名、手机号、邮箱）
- `status`: 状态筛选（0-冻结 1-正常）
- `userType`: 用户类型筛选（1-普通用户 2-商户管理员 3-社区管理员）

**响应示例**：
```json
{
  "code": 200,
  "data": {
    "records": [
      {
        "userId": 1,
        "userName": "张三",
        "phone": "13800138000",
        "email": "zhangsan@example.com",
        "avatar": "http://...",
        "gender": 1,
        "age": 25,
        "userType": 1,
        "status": 1,
        "roles": ["ROLE_USER"],
        "createTime": "2026-01-01 10:00:00",
        "updateTime": "2026-01-05 14:00:00"
      }
    ],
    "total": 100,
    "current": 1,
    "size": 10
  }
}
```

### 2. 获取用户详情

```http
GET /admin/users/{userId}
Authorization: Bearer {admin_token}
```

**响应示例**：
```json
{
  "code": 200,
  "data": {
    "userId": 1,
    "userName": "张三",
    "phone": "13800138000",
    "email": "zhangsan@example.com",
    "avatar": "http://...",
    "gender": 1,
    "age": 25,
    "userType": 1,
    "status": 1,
    "roles": [
      {
        "roleId": 1,
        "roleName": "普通用户",
        "roleCode": "ROLE_USER"
      }
    ],
    "wallet": {
      "balance": 1000.00,
      "totalRecharge": 5000.00,
      "totalExpense": 4000.00
    },
    "createTime": "2026-01-01 10:00:00",
    "updateTime": "2026-01-05 14:00:00"
  }
}
```

### 3. 更新用户状态

```http
PUT /admin/users/{userId}/status
Authorization: Bearer {admin_token}
Content-Type: application/json

{
  "status": 0
}
```

**说明**：
- `status`: 0-冻结 1-正常
- 冻结后用户无法登录
- 超级管理员账号不能被冻结

### 4. 分配角色

```http
POST /admin/users/{userId}/roles
Authorization: Bearer {admin_token}
Content-Type: application/json

{
  "roleIds": [1, 2, 3]
}
```

**说明**：
- 覆盖用户现有角色
- 只能分配已存在的角色

### 5. 更新用户信息

```http
PUT /admin/users/{userId}
Authorization: Bearer {admin_token}
Content-Type: application/json

{
  "userName": "张三",
  "email": "zhangsan@example.com",
  "gender": 1,
  "age": 25,
  "avatar": "http://..."
}
```

**说明**：
- 所有字段都是可选的
- 不能修改密码和手机号

### 6. 删除用户

```http
DELETE /admin/users/{userId}
Authorization: Bearer {admin_token}
```

**说明**：
- 软删除，将状态设为冻结
- 超级管理员账号不能删除

## 使用场景

### 场景 1：查找并冻结违规用户

```bash
# 1. 搜索用户
GET /admin/users?keyword=13800138000

# 2. 查看用户详情
GET /admin/users/123

# 3. 冻结用户
PUT /admin/users/123/status
{
  "status": 0
}
```

### 场景 2：为用户升级为管理员

```bash
# 1. 查看用户当前角色
GET /admin/users/123

# 2. 分配管理员角色
POST /admin/users/123/roles
{
  "roleIds": [1, 2]  # 普通用户 + 社区管理员
}
```

### 场景 3：批量查询特定类型用户

```bash
# 查询所有社区管理员
GET /admin/users?userType=3&page=1&size=20
```

## 业务规则

### 1. 状态管理
- **冻结用户**：用户无法登录系统
- **启用用户**：恢复用户正常使用
- **保护规则**：超级管理员账号不能被冻结

### 2. 角色分配
- 覆盖式分配：新角色会替换所有现有角色
- 角色验证：只能分配系统中已存在的角色
- 权限继承：用户获得所分配角色的所有权限

### 3. 信息更新
- **可更新字段**：用户名、邮箱、性别、年龄、头像
- **不可更新字段**：密码、手机号（需要单独接口）
- **部分更新**：只更新提供的字段

### 4. 删除规则
- **软删除**：不删除数据，只将状态设为冻结
- **保护规则**：超级管理员账号不能删除
- **关联数据**：钱包等关联数据保留

## 数据字段说明

### 用户类型（userType）
- `1` - 普通用户
- `2` - 商户管理员
- `3` - 社区管理员

### 用户状态（status）
- `0` - 冻结
- `1` - 正常

### 性别（gender）
- `0` - 未知
- `1` - 男
- `2` - 女

## 注意事项

1. **权限验证**：所有接口都需要管理员权限
2. **操作日志**：重要操作会记录日志
3. **并发安全**：使用事务保证数据一致性
4. **敏感信息**：密码字段不会返回给前端
5. **性能优化**：
   - 列表查询使用索引
   - 关联查询优化
   - 分页查询限制

## 常见问题

### Q1: 为什么不能冻结某个用户？

可能是超级管理员账号，系统禁止冻结超级管理员。

### Q2: 分配角色后用户权限没有生效？

用户需要重新登录才能获得新角色的权限。

### Q3: 如何批量管理用户？

目前需要逐个操作，后续可以添加批量操作接口。

### Q4: 删除用户后能恢复吗？

可以，删除是软删除，只需将状态改回正常即可。

## 最佳实践

1. **定期审查**：定期查看用户列表，清理异常账号
2. **权限最小化**：只分配必要的角色和权限
3. **操作记录**：重要操作前先查看用户详情
4. **谨慎删除**：删除前确认用户信息，避免误操作

---

如有任何问题，请查阅项目文档或联系开发团队。
