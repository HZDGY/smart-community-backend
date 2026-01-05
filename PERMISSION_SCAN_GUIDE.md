# 权限注解自动扫描功能使用指南

## 功能介绍

权限注解自动扫描功能可以自动扫描项目中所有 Controller 的 `@RequirePermission` 注解，并将扫描到的权限自动存入数据库，大大简化权限管理工作。

### 核心优势

- ✅ **自动化**：无需手动在数据库中添加权限
- ✅ **智能命名**：自动生成中文权限名称和描述
- ✅ **幂等性**：可重复执行，不会产生重复数据
- ✅ **实时同步**：添加新功能后立即同步权限

## 使用方法

### 1. 在代码中添加权限注解

在 Controller 方法上添加 `@RequirePermission` 注解：

```java
@PostMapping("/export")
@RequirePermission("report:export")
public Result<Void> exportReport() {
    // 导出报表逻辑
}
```

### 2. 调用扫描接口

使用超级管理员账号调用扫描接口：

```http
POST /api/permission/scan
Authorization: Bearer {super_admin_token}
```

### 3. 查看扫描结果

**响应示例**：

```json
{
  "code": 200,
  "message": "权限扫描完成",
  "data": {
    "totalScanned": 26,
    "newAdded": 1,
    "alreadyExists": 25,
    "controllerCount": 8,
    "methodCount": 156,
    "newPermissions": [
      "report:export"
    ],
    "existingPermissions": [
      "user:view",
      "user:create",
      "complaint:audit",
      ...
    ]
  }
}
```

**字段说明**：
- `totalScanned`: 扫描到的权限总数
- `newAdded`: 新增的权限数量
- `alreadyExists`: 已存在的权限数量
- `controllerCount`: 扫描的控制器数量
- `methodCount`: 扫描的方法总数
- `newPermissions`: 新增的权限列表
- `existingPermissions`: 已存在的权限列表

## 权限命名规则

系统会根据权限编码自动生成中文名称和描述：

| 权限编码 | 自动生成的名称 | 自动生成的描述 |
|---------|--------------|--------------|
| `user:view` | 查看用户 | 查看用户权限 |
| `user:create` | 创建用户 | 创建用户权限 |
| `complaint:audit` | 审核投诉 | 审核投诉权限 |
| `report:export` | 导出报表 | 导出报表权限 |

### 支持的资源类型

- `user` - 用户
- `role` - 角色
- `permission` - 权限
- `complaint` - 投诉
- `repair` - 报修
- `parking` - 停车
- `announcement` - 公告
- `visitor` - 访客
- `mall` - 商品
- `order` - 订单
- `report` - 报表
- `data` - 数据
- `system` - 系统

### 支持的操作类型

- `view` - 查看
- `create` - 创建
- `update` - 更新
- `delete` - 删除
- `audit` - 审核
- `handle` - 处理
- `approve` - 审批
- `manage` - 管理
- `export` - 导出
- `import` - 导入
- `backup` - 备份
- `restore` - 恢复

### 扩展映射表

如果需要添加新的资源或操作类型，可以在代码中扩展：

```java
// 添加新的资源映射
PermissionNameGenerator.addResourceMapping("invoice", "发票");

// 添加新的操作映射
PermissionNameGenerator.addActionMapping("print", "打印");
```

## 使用场景

### 场景 1：项目初始化

在项目第一次部署时，执行一次扫描，将所有权限同步到数据库：

```bash
# 1. 启动项目
mvn spring-boot:run

# 2. 使用超级管理员登录并获取 token

# 3. 调用扫描接口
curl -X POST http://localhost:8080/api/permission/scan \
  -H "Authorization: Bearer {token}"
```

### 场景 2：添加新功能

开发人员添加了新的功能模块：

```java
@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {
    
    @GetMapping("/list")
    @RequirePermission("invoice:view")
    public Result<List<Invoice>> getInvoices() {
        // ...
    }
    
    @PostMapping("/export")
    @RequirePermission("invoice:export")
    public Result<Void> exportInvoice() {
        // ...
    }
}
```

部署后调用扫描接口，新权限会自动添加到数据库。

### 场景 3：定期同步

建议在每次发布新版本后执行一次扫描，确保权限数据与代码保持同步。

## 注意事项

### 1. 权限编码规范

确保所有权限编码遵循 `资源:操作` 格式：

✅ 正确：`user:view`、`complaint:audit`、`report:export`  
❌ 错误：`userView`、`audit_complaint`、`export-report`

### 2. 访问权限

扫描接口需要超级管理员权限（`ROLE_SUPER_ADMIN`），普通用户无法访问。

### 3. 幂等性

扫描接口可以重复调用，已存在的权限不会重复插入，只会添加新权限。

### 4. 性能考虑

扫描过程会遍历所有 Controller 和方法，建议在非高峰期执行。

### 5. 事务处理

扫描和保存操作在同一个事务中执行，如果出现错误会自动回滚。

## 验证步骤

### 1. 查看扫描日志

扫描过程会输出详细日志：

```
2026-01-05 11:00:00 INFO  PermissionScanServiceImpl - 开始扫描权限注解...
2026-01-05 11:00:00 INFO  PermissionScanServiceImpl - 找到 8 个 Controller
2026-01-05 11:00:01 INFO  PermissionScanServiceImpl - 扫描到 156 个方法，共 26 个权限
2026-01-05 11:00:01 INFO  PermissionScanServiceImpl - 新增权限：report:export (导出报表)
2026-01-05 11:00:01 INFO  PermissionScanServiceImpl - 权限扫描完成：新增 1 个，已存在 25 个
```

### 2. 查询数据库

验证权限已正确插入：

```sql
-- 查看所有权限
SELECT * FROM sys_permission ORDER BY create_time DESC;

-- 查看最近添加的权限
SELECT * FROM sys_permission 
WHERE create_time > DATE_SUB(NOW(), INTERVAL 1 HOUR)
ORDER BY create_time DESC;

-- 统计权限数量
SELECT COUNT(*) FROM sys_permission;
```

### 3. 测试权限控制

使用新权限测试访问控制：

```java
@GetMapping("/test")
@RequirePermission("report:export")
public Result<String> test() {
    return success("测试成功");
}
```

使用没有该权限的用户访问，应该返回 403 错误。

## 常见问题

### Q1: 扫描不到某些权限？

**可能原因**：
- 方法不是 public 的
- Controller 类没有 `@RestController` 或 `@Controller` 注解
- 注解写错了（如 `@RequireRole` 而不是 `@RequirePermission`）

**解决方法**：检查方法和类的注解是否正确。

### Q2: 权限名称生成不正确？

**可能原因**：资源或操作类型不在映射表中。

**解决方法**：使用 `PermissionNameGenerator.addResourceMapping()` 或 `addActionMapping()` 添加映射。

### Q3: 如何删除废弃的权限？

目前需要手动删除。未来版本可能会添加自动检测废弃权限的功能。

```sql
-- 手动删除权限
DELETE FROM sys_permission WHERE permission_code = 'old:permission';
```

### Q4: 扫描接口返回 403？

确保使用的是超级管理员账号，并且该账号拥有 `ROLE_SUPER_ADMIN` 角色。

## 最佳实践

### 1. 定期扫描

建议在以下时机执行扫描：
- 项目初始化时
- 每次发布新版本后
- 添加新功能模块后

### 2. 权限命名规范

统一使用 `资源:操作` 格式，保持命名一致性：
- 资源名使用单数形式：`user` 而不是 `users`
- 操作名使用动词：`view`、`create`、`update`、`delete`

### 3. 文档同步

在添加新权限时，同时更新权限文档，说明权限的用途和使用场景。

### 4. 权限分组

将相关权限归类到同一资源下，便于管理：
- 用户管理：`user:view`、`user:create`、`user:update`、`user:delete`
- 报表管理：`report:view`、`report:export`、`report:print`

## 总结

权限注解自动扫描功能大大简化了权限管理工作，开发人员只需要在代码中添加注解，然后调用扫描接口即可自动同步到数据库。这不仅提高了开发效率，还减少了手动操作带来的错误。

---

如有任何问题，请查阅项目文档或联系开发团队。
