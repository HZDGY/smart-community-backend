# 用户钱包和物业费系统使用指南

## 功能概述

用户钱包和物业费系统提供完整的钱包管理和物业费缴纳功能，支持充值、转账、查看账单和在线缴费。

## 快速开始

### 1. 初始化数据库

执行数据库脚本：

```bash
# 创建钱包和物业费表
mysql -u root -p your_database < wallet_system_init.sql

# 配置权限
mysql -u root -p your_database < wallet_permissions.sql
```

### 2. 重启项目

```bash
mvn spring-boot:run
```

### 3. 访问 API 文档

打开 `http://localhost:8080/doc.html` 查看钱包管理和物业费管理接口。

## 钱包功能

### 查看钱包信息

```http
GET /wallet/info
Authorization: Bearer {token}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "walletId": 1,
    "userId": 10,
    "balance": 1000.00,
    "frozenAmount": 0.00,
    "totalRecharge": 5000.00,
    "totalExpense": 4000.00,
    "status": 1
  }
}
```

### 充值

```http
POST /wallet/recharge
Authorization: Bearer {token}
Content-Type: application/json

{
  "amount": 100.00,
  "paymentMethod": "ALIPAY"
}
```

**说明**：
- `amount`: 充值金额（0.01 - 10000）
- `paymentMethod`: 支付方式（ALIPAY-支付宝 / WECHAT-微信）

### 转账

```http
POST /wallet/transfer
Authorization: Bearer {token}
Content-Type: application/json

{
  "toUserId": 20,
  "amount": 50.00,
  "description": "转账给朋友"
}
```

**说明**：
- `toUserId`: 收款用户ID
- `amount`: 转账金额
- `description`: 转账描述（可选）

### 查询交易记录

```http
GET /wallet/transactions?type=ALL&page=1&size=10
Authorization: Bearer {token}
```

**参数**：
- `type`: 交易类型（ALL-全部 / RECHARGE-充值 / TRANSFER_OUT-转出 / TRANSFER_IN-转入 / PAYMENT-支付）
- `page`: 页码
- `size`: 每页数量

## 物业费功能

### 查询物业费账单

```http
GET /property-fee/bills?status=0&page=1&size=10
Authorization: Bearer {token}
```

**参数**：
- `status`: 账单状态（0-未缴 / 1-已缴 / 2-部分缴纳）
- `page`: 页码
- `size`: 每页数量

**响应示例**：
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "billId": 1,
      "billNo": "BILL20260105134500A1B2C3D4",
      "userId": 10,
      "billingPeriod": "2026-01",
      "propertyFee": 300.00,
      "waterFee": 50.00,
      "electricityFee": 100.00,
      "gasFee": 30.00,
      "parkingFee": 200.00,
      "otherFee": 0.00,
      "totalAmount": 680.00,
      "paidAmount": 0.00,
      "status": 0,
      "dueDate": "2026-02-01"
    }
  ],
  "total": 1
}
```

### 获取账单详情

```http
GET /property-fee/bills/{billId}
Authorization: Bearer {token}
```

### 缴纳物业费

```http
POST /property-fee/pay
Authorization: Bearer {token}
Content-Type: application/json

{
  "billId": 1,
  "amount": 680.00,
  "paymentMethod": "WALLET"
}
```

**说明**：
- `billId`: 账单ID
- `amount`: 缴费金额（可以部分缴纳）
- `paymentMethod`: 支付方式（WALLET-钱包 / ALIPAY-支付宝 / WECHAT-微信）

### 查询缴费记录

```http
GET /property-fee/payments?page=1&size=10
Authorization: Bearer {token}
```

### 生成物业费账单（管理员）

```http
POST /property-fee/bills/generate
Authorization: Bearer {admin_token}
Content-Type: application/json

{
  "userId": 10,
  "billingPeriod": "2026-01",
  "propertyFee": 300.00,
  "waterFee": 50.00,
  "electricityFee": 100.00,
  "gasFee": 30.00,
  "parkingFee": 200.00,
  "otherFee": 0.00,
  "dueDate": "2026-02-01"
}
```

## 权限说明

### 钱包权限
- `wallet:view` - 查看钱包（普通用户、管理员）
- `wallet:recharge` - 充值（普通用户）
- `wallet:transfer` - 转账（普通用户）

### 物业费权限
- `property-fee:view` - 查看物业费（普通用户、管理员）
- `property-fee:pay` - 缴纳物业费（普通用户）
- `property-fee:manage` - 管理物业费（社区管理员、超级管理员）

## 使用流程示例

### 场景 1：用户充值并缴纳物业费

1. **查看钱包余额**
   ```http
   GET /wallet/info
   ```

2. **充值到钱包**
   ```http
   POST /wallet/recharge
   {
     "amount": 1000.00,
     "paymentMethod": "ALIPAY"
   }
   ```

3. **查看物业费账单**
   ```http
   GET /property-fee/bills?status=0
   ```

4. **使用钱包缴纳物业费**
   ```http
   POST /property-fee/pay
   {
     "billId": 1,
     "amount": 680.00,
     "paymentMethod": "WALLET"
   }
   ```

5. **查看缴费记录**
   ```http
   GET /property-fee/payments
   ```

### 场景 2：用户之间转账

1. **用户A向用户B转账**
   ```http
   POST /wallet/transfer
   {
     "toUserId": 20,
     "amount": 100.00,
     "description": "还钱"
   }
   ```

2. **查看交易记录**
   ```http
   GET /wallet/transactions?type=TRANSFER_OUT
   ```

### 场景 3：管理员生成物业费账单

1. **生成账单**
   ```http
   POST /property-fee/bills/generate
   {
     "userId": 10,
     "billingPeriod": "2026-02",
     "propertyFee": 300.00,
     "waterFee": 60.00,
     "electricityFee": 120.00,
     "dueDate": "2026-03-01"
   }
   ```

## 注意事项

1. **充值限制**：单次充值金额 0.01 - 10000 元
2. **转账限制**：不能给自己转账，余额必须充足
3. **缴费规则**：可以部分缴纳，支持多次缴费
4. **并发安全**：转账和缴费使用数据库行锁，保证并发安全
5. **交易流水号**：所有交易都有唯一流水号，可追溯

## 常见问题

### Q1: 充值后余额没有变化？

检查充值是否成功，查看交易记录确认。

### Q2: 转账失败提示余额不足？

确保钱包余额大于转账金额。

### Q3: 缴纳物业费时提示金额超过应缴金额？

检查账单的总金额和已缴金额，确保缴费金额不超过剩余应缴金额。

### Q4: 如何查看某个月的物业费账单？

使用账单列表接口，根据 `billingPeriod` 字段筛选。

## 技术架构

```
请求 → JWT拦截器 → 权限切面 → 控制器 → 服务层 → Mapper → 数据库
```

### 并发控制

转账和缴费使用 `FOR UPDATE` 行锁：

```java
UserWallet wallet = walletMapper.selectByUserIdForUpdate(userId);
```

### 事务管理

所有涉及金额变动的操作都使用 `@Transactional` 保证事务一致性。

---

如有任何问题，请查阅项目文档或联系开发团队。
