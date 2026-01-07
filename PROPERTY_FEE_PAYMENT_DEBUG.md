# 物业费支付问题调试指南

## 问题描述
使用钱包支付物业费时，账单状态更新了，但钱包余额没有扣减。

## 已修复内容
1. 添加了详细的日志输出
2. 增强了支付方式的判断逻辑
3. 确保钱包支付一定会扣款

## 测试步骤

### 前置条件
1. 确保用户钱包有足够余额
2. 确保有未缴纳的物业费账单

### 测试流程

#### 1. 查看钱包余额
```bash
GET /api/wallet/info
Authorization: Bearer {token}
```

**记录余额：** 假设为 1000.00 元

#### 2. 查看物业费账单
```bash
GET /api/property-fee/bills
Authorization: Bearer {token}
```

**记录账单：** 
- billId: 8
- totalAmount: 728.10
- status: 0 (未缴)

#### 3. 创建支付订单（钱包支付）
```bash
POST /api/payment/create-order
Authorization: Bearer {token}
Content-Type: application/json

{
  "orderType": "PROPERTY_FEE",
  "amount": 728.10,
  "paymentMethod": "WALLET",
  "relatedId": 8,
  "description": "缴纳物业费"
}
```

**记录订单号：** ORDER20260106XXXXXX

#### 4. 发起支付
```bash
POST /api/payment/initiate/ORDER20260106XXXXXX
Authorization: Bearer {token}
```

#### 5. 模拟支付成功
```bash
POST /api/payment/mock/callback/ORDER20260106XXXXXX?success=true
Authorization: Bearer {token}
```

**关键日志检查：**
```
=== 物业费支付成功处理开始 ===
订单号: ORDER20260106XXXXXX
支付方式: WALLET
用户ID: 13
账单ID: 8
金额: 728.10
检查支付方式: paymentMethod=[WALLET], WALLET=[WALLET], equals=true
===== 开始扣除钱包余额 =====
用户ID: 13, 金额: 728.10, 订单号: ORDER20260106XXXXXX
用户 13 支付成功，金额: 728.10，订单号: ORDER20260106XXXXXX
===== 钱包扣款成功 =====
交易ID: XXX, 交易号: WTXN20260106XXXXXX
开始更新物业费账单状态...
第三方支付成功处理完成：用户 13，账单 8，金额 728.10，支付方式 WALLET
物业费账单更新成功，缴费记录ID: XXX
=== 物业费支付处理完成：用户 13，账单 8，金额 728.10 ===
模拟支付成功：订单号 ORDER20260106XXXXXX
```

#### 6. 再次查看钱包余额
```bash
GET /api/wallet/info
Authorization: Bearer {token}
```

**预期结果：** 余额应该变为 1000.00 - 728.10 = 271.90 元

#### 7. 查看物业费账单状态
```bash
GET /api/property-fee/bills/8
Authorization: Bearer {token}
```

**预期结果：**
- status: 1 (已缴)
- paidAmount: 728.10

#### 8. 查看钱包交易记录
```bash
GET /api/wallet/transactions?page=1&size=10
Authorization: Bearer {token}
```

**预期结果：** 应该看到一条支付记录
```json
{
  "transactionType": "PAYMENT",
  "amount": 728.10,
  "description": "缴纳物业费",
  "relatedOrderNo": "ORDER20260106XXXXXX"
}
```

## 可能的问题

### 问题1：日志中没有 "检查支付方式" 这一行
**原因：** 代码没有重新编译或应用没有重启
**解决：**
```bash
mvn clean compile
# 然后重启应用
```

### 问题2：日志显示 "非钱包支付，跳过钱包扣款"
**原因：** paymentMethod 字段值不是 "WALLET"
**检查：** 
1. 创建订单时的 paymentMethod 是否正确
2. 数据库中 payment_order 表的 payment_method 字段值

```sql
SELECT order_id, order_no, payment_method, order_type, amount, status
FROM payment_order
WHERE order_no = 'ORDER20260106XXXXXX';
```

### 问题3：钱包余额不足
**错误日志：** "余额不足"
**解决：** 先充值钱包
```bash
POST /api/payment/create-order
{
  "orderType": "RECHARGE",
  "amount": 1000.00,
  "paymentMethod": "ALIPAY",
  "description": "钱包充值"
}
```

### 问题4：账单已缴清
**日志：** "账单已缴清，跳过处理"
**解决：** 查询其他未缴账单或生成新账单

## 验证清单

- [ ] 应用已重启，使用的是最新代码
- [ ] 钱包有足够余额
- [ ] 账单是未缴状态
- [ ] 创建订单时 paymentMethod 是 "WALLET"
- [ ] 支付成功后查看日志是否有 "钱包扣款成功"
- [ ] 钱包余额已减少
- [ ] 账单状态已更新
- [ ] 钱包交易记录中有支付记录
- [ ] 物业费缴费记录已创建

## 数据库验证

### 1. 检查支付订单
```sql
SELECT * FROM payment_order WHERE order_no = 'ORDER20260106XXXXXX';
```

### 2. 检查钱包余额
```sql
SELECT wallet_id, user_id, balance, total_expense 
FROM user_wallet 
WHERE user_id = 13;
```

### 3. 检查钱包交易记录
```sql
SELECT * FROM wallet_transaction 
WHERE user_id = 13 
ORDER BY create_time DESC 
LIMIT 5;
```

### 4. 检查物业费账单
```sql
SELECT * FROM property_fee_bill WHERE bill_id = 8;
```

### 5. 检查物业费缴费记录
```sql
SELECT * FROM property_fee_payment 
WHERE bill_id = 8 
ORDER BY create_time DESC;
```

## 直接使用钱包支付（推荐方式）

如果支付订单流程有问题，可以直接使用钱包支付接口：

```bash
POST /api/property-fee/pay
Authorization: Bearer {token}
Content-Type: application/json

{
  "billId": 8,
  "amount": 728.10,
  "paymentMethod": "WALLET"
}
```

这个接口会：
1. ✅ 直接扣除钱包余额
2. ✅ 更新账单状态
3. ✅ 创建缴费记录
4. ✅ 记录钱包交易

## 联系支持

如果问题仍然存在，请提供：
1. 完整的日志（从创建订单到支付成功）
2. 数据库查询结果（上面的5个SQL）
3. 创建订单时的请求参数

