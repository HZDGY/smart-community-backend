# 权限管理系统使用指南

## 系统概述

本权限管理系统采用 **RBAC（基于角色的访问控制）** 模型，通过 **AOP 切面 + 自定义注解** 的方式实现细粒度的权限控制。

### 核心特性

- ✅ **注解驱动**：使用 `@RequirePermission` 和 `@RequireRole` 注解即可控制接口权限
- ✅ **高度灵活**：支持单个权限、多个权限（AND/OR）、角色验证等多种场景
- ✅ **细粒度控制**：权限采用 `资源:操作` 格式（如 `user:create`、`complaint:audit`）
- ✅ **易于扩展**：支持动态添加角色和权限

## 快速开始

### 1. 初始化数据库

执行项目根目录下的 `permission_system_init.sql` 脚本：

```bash
mysql -u root -p your_database < permission_system_init.sql
```

该脚本会：
- 创建权限表和角色权限关联表
- 初始化 4 个默认角色（超级管理员、社区管理员、商户管理员、普通用户）
- 初始化 30+ 个默认权限
- 为各角色分配相应权限

### 2. 为用户分配角色

使用权限管理接口为用户分配角色：

```http
POST /api/permission/user/assign-roles
Content-Type: application/json
Authorization: Bearer {admin_token}

{
  "userId": 1,
  "roleIds": [1, 4]
}
```

## 注解使用指南

### @RequirePermission 注解

用于方法级别的权限控制。

#### 单个权限

```java
@GetMapping("/users")
@RequirePermission("user:view")
public Result<List<User>> getUsers() {
    // 只有拥有 user:view 权限的用户才能访问
}
```

#### 多个权限（AND - 必须拥有所有权限）

```java
@PutMapping("/users/{id}")
@RequirePermission(value = {"user:view", "user:update"}, logical = Logical.AND)
public Result<Void> updateUser(@PathVariable Long id) {
    // 必须同时拥有 user:view 和 user:update 权限
}
```

#### 多个权限（OR - 拥有任意一个即可）

```java
@GetMapping("/reports")
@RequirePermission(value = {"complaint:view", "repair:view"}, logical = Logical.OR)
public Result<List<Report>> getReports() {
    // 拥有 complaint:view 或 repair:view 任意一个权限即可
}
```

### @RequireRole 注解

用于方法级别的角色控制。

#### 单个角色

```java
@DeleteMapping("/users/{id}")
@RequireRole("ROLE_SUPER_ADMIN")
public Result<Void> deleteUser(@PathVariable Long id) {
    // 只有超级管理员才能访问
}
```

#### 多个角色（OR - 默认）

```java
@GetMapping("/admin/dashboard")
@RequireRole(value = {"ROLE_SUPER_ADMIN", "ROLE_COMMUNITY_ADMIN"}, logical = Logical.OR)
public Result<Dashboard> getDashboard() {
    // 超级管理员或社区管理员都可以访问
}
```

#### 多个角色（AND）

```java
@PostMapping("/critical-operation")
@RequireRole(value = {"ROLE_SUPER_ADMIN", "ROLE_AUDITOR"}, logical = Logical.AND)
public Result<Void> criticalOperation() {
    // 必须同时拥有两个角色
}
```

## 权限命名规范

权限编码采用 `资源:操作` 格式：

| 权限编码 | 说明 | 示例场景 |
|---------|------|---------|
| `user:view` | 查看用户 | 查询用户列表、查看用户详情 |
| `user:create` | 创建用户 | 注册新用户 |
| `user:update` | 更新用户 | 修改用户信息 |
| `user:delete` | 删除用户 | 删除用户账号 |
| `complaint:audit` | 审核投诉 | 审核用户投诉 |
| `parking:approve` | 审批停车 | 审批停车申请 |

## API 接口文档

### 权限管理接口

#### 获取所有权限

```http
GET /api/permission/list
Authorization: Bearer {token}
```

**需要权限**：`permission:view`

#### 创建权限

```http
POST /api/permission/create
Content-Type: application/json
Authorization: Bearer {token}

{
  "permissionName": "导出报表",
  "permissionCode": "report:export",
  "resourceType": "api",
  "description": "导出统计报表"
}
```

**需要权限**：`permission:create`

### 角色管理接口

#### 获取所有角色

```http
GET /api/permission/role/list
Authorization: Bearer {token}
```

**需要权限**：`role:view`

#### 创建角色

```http
POST /api/permission/role/create
Content-Type: application/json
Authorization: Bearer {token}

{
  "roleName": "财务管理员",
  "roleCode": "ROLE_FINANCE_ADMIN",
  "description": "财务相关权限"
}
```

**需要权限**：`role:create`

#### 为角色分配权限

```http
POST /api/permission/role/assign-permissions
Content-Type: application/json
Authorization: Bearer {token}

{
  "roleId": 5,
  "permissionIds": [1, 2, 3, 15, 16]
}
```

**需要权限**：`role:assign-permission`

### 用户权限接口

#### 为用户分配角色

```http
POST /api/permission/user/assign-roles
Content-Type: application/json
Authorization: Bearer {token}

{
  "userId": 10,
  "roleIds": [2, 4]
}
```

**需要权限**：`user:assign-role`

#### 获取用户权限信息

```http
GET /api/permission/user/{userId}
Authorization: Bearer {token}
```

**需要权限**：`user:view-permission`

**响应示例**：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "userId": 10,
    "roles": [
      {
        "roleId": 2,
        "roleName": "社区管理员",
        "roleCode": "ROLE_COMMUNITY_ADMIN",
        "description": "社区管理相关权限",
        "status": 1
      }
    ],
    "permissions": [
      {
        "permissionId": 1,
        "permissionName": "查看用户",
        "permissionCode": "user:view",
        "resourceType": "api",
        "description": "查看用户信息",
        "status": 1
      }
    ]
  }
}
```

#### 获取当前用户权限信息

```http
GET /api/permission/user/current
Authorization: Bearer {token}
```

无需特殊权限，任何登录用户都可以查看自己的权限。

## 默认角色和权限

### 默认角色

| 角色名称 | 角色编码 | 说明 |
|---------|---------|------|
| 超级管理员 | `ROLE_SUPER_ADMIN` | 拥有系统所有权限 |
| 社区管理员 | `ROLE_COMMUNITY_ADMIN` | 社区管理相关权限 |
| 商户管理员 | `ROLE_MERCHANT_ADMIN` | 商城管理相关权限 |
| 普通用户 | `ROLE_USER` | 基础用户权限 |

### 权限分类

#### 用户管理权限
- `user:view` - 查看用户
- `user:create` - 创建用户
- `user:update` - 更新用户
- `user:delete` - 删除用户
- `user:assign-role` - 分配角色
- `user:view-permission` - 查看用户权限

#### 角色管理权限
- `role:view` - 查看角色
- `role:create` - 创建角色
- `role:update` - 更新角色
- `role:delete` - 删除角色
- `role:assign-permission` - 分配权限

#### 权限管理权限
- `permission:view` - 查看权限
- `permission:create` - 创建权限
- `permission:update` - 更新权限
- `permission:delete` - 删除权限

#### 业务权限
- 投诉管理：`complaint:view`、`complaint:audit`、`complaint:handle`
- 报修管理：`repair:view`、`repair:audit`、`repair:handle`
- 停车管理：`parking:view`、`parking:approve`
- 公告管理：`announcement:view`、`announcement:create`、`announcement:update`、`announcement:delete`
- 访客管理：`visitor:view`、`visitor:audit`
- 商城管理：`mall:view`、`mall:manage`、`order:view`、`order:handle`

## 实际应用示例

### 示例 1：投诉管理

```java
@RestController
@RequestMapping("/api/complaint")
public class ComplaintController {
    
    // 普通用户可以查看投诉
    @GetMapping("/list")
    @RequirePermission("complaint:view")
    public Result<List<Complaint>> getComplaints() {
        // ...
    }
    
    // 只有管理员可以审核投诉
    @PostMapping("/{id}/audit")
    @RequirePermission("complaint:audit")
    public Result<Void> auditComplaint(@PathVariable Long id) {
        // ...
    }
    
    // 只有管理员可以处理投诉
    @PostMapping("/{id}/handle")
    @RequirePermission("complaint:handle")
    public Result<Void> handleComplaint(@PathVariable Long id) {
        // ...
    }
}
```

### 示例 2：用户管理

```java
@RestController
@RequestMapping("/api/admin/users")
public class UserAdminController {
    
    // 查看用户列表
    @GetMapping
    @RequirePermission("user:view")
    public Result<List<User>> getUsers() {
        // ...
    }
    
    // 更新用户需要同时拥有查看和更新权限
    @PutMapping("/{id}")
    @RequirePermission(value = {"user:view", "user:update"}, logical = Logical.AND)
    public Result<Void> updateUser(@PathVariable Long id) {
        // ...
    }
    
    // 删除用户只有超级管理员可以操作
    @DeleteMapping("/{id}")
    @RequireRole("ROLE_SUPER_ADMIN")
    public Result<Void> deleteUser(@PathVariable Long id) {
        // ...
    }
}
```

## 常见问题

### Q1: 如何为新用户分配默认角色？

在用户注册时，可以自动为其分配"普通用户"角色：

```java
@Service
public class UserServiceImpl {
    
    @Autowired
    private PermissionService permissionService;
    
    public void registerUser(UserRegisterRequest request) {
        // 创建用户
        SysUser user = createUser(request);
        
        // 分配默认角色（普通用户）
        Long userRoleId = 4L; // ROLE_USER 的 ID
        permissionService.assignRolesToUser(user.getUserId(), List.of(userRoleId));
    }
}
```

### Q2: 权限验证失败会返回什么？

权限验证失败会抛出 `PermissionDeniedException`，全局异常处理器会捕获并返回 403 状态码：

```json
{
  "code": 403,
  "message": "权限不足：需要权限 [user:delete]",
  "data": null
}
```

### Q3: 如何在代码中手动检查权限？

可以注入 `PermissionService` 手动检查：

```java
@Service
public class SomeService {
    
    @Autowired
    private PermissionService permissionService;
    
    public void doSomething(Long userId) {
        if (permissionService.hasPermission(userId, "user:delete")) {
            // 有权限，执行操作
        } else {
            // 无权限，抛出异常或返回错误
            throw new PermissionDeniedException("权限不足");
        }
    }
}
```

### Q4: 如何添加新的权限？

通过权限管理接口添加：

```http
POST /api/permission/create
Content-Type: application/json
Authorization: Bearer {admin_token}

{
  "permissionName": "导出数据",
  "permissionCode": "data:export",
  "resourceType": "api",
  "description": "导出系统数据"
}
```

然后将新权限分配给相应的角色。

### Q5: 超级管理员如何获取所有权限？

在数据库初始化脚本中，已经为超级管理员分配了所有权限。如果后续添加了新权限，需要手动为超级管理员分配。

## 测试建议

### 1. 使用 Knife4j 测试

访问 `http://localhost:8080/doc.html`，使用 Knife4j 界面测试权限接口。

### 2. 测试流程

1. 使用管理员账号登录，获取 token
2. 创建新角色和权限
3. 为角色分配权限
4. 为测试用户分配角色
5. 使用测试用户的 token 访问需要权限的接口
6. 验证权限控制是否生效

### 3. 测试用例

- ✅ 拥有权限的用户可以正常访问
- ✅ 没有权限的用户返回 403
- ✅ 未登录用户返回 401
- ✅ AND 逻辑：必须拥有所有权限
- ✅ OR 逻辑：拥有任意一个权限即可

## 技术架构

```
请求 → JWT拦截器（认证） → 权限切面（授权） → 控制器方法
```

1. **JWT 拦截器**：验证 token 有效性，提取用户信息
2. **权限切面**：拦截带有权限注解的方法，验证用户权限
3. **控制器方法**：执行业务逻辑

## 注意事项

1. **权限注解只能用于 Spring 管理的 Bean**：确保控制器类使用了 `@RestController` 或 `@Controller` 注解
2. **方法必须是 public**：AOP 切面只能拦截 public 方法
3. **避免在同一个类内部调用**：AOP 代理不会拦截内部方法调用
4. **权限编码区分大小写**：确保权限编码的大小写一致

## 后续扩展

### 1. 添加权限缓存

使用 Spring Cache 缓存用户权限，提高性能：

```java
@Cacheable(value = "userPermissions", key = "#userId")
public Set<String> getUserPermissions(Long userId) {
    // ...
}
```

### 2. 添加数据权限

扩展权限系统，支持数据级别的权限控制（如只能查看自己部门的数据）。

### 3. 添加权限审计日志

记录权限变更和访问日志，用于安全审计。

---

如有任何问题，请查阅项目文档或联系开发团队。
