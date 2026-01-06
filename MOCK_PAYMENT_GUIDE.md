# 模拟支付系统使用指南

## 概述

本系统实现了完整的模拟支付功能，支持支付宝、微信和钱包三种支付方式。适用于开发和测试环境。

## 功能特性

✅ **支付订单管理** - 创建、查询、取消支付订单  
✅ **多种支付方式** - 支付宝、微信、钱包  
✅ **模拟支付回调** - 模拟第三方支付成功/失败回调  
✅ **订单状态跟踪** - 待支付、支付中、成功、失败、已取消  
✅ **自动过期处理** - 订单30分钟自动过期  
✅ **业务集成** - 充值和物业费支付已集成

## 数据库初始化

执行以下SQL脚本：

```bash
mysql -u root -p your_database < payment_system_enhancement.sql
```

## API接口说明

### 1. 钱包充值（新流程）

#### 步骤1：创建充值订单

```http
POST /wallet/recharge
Authorization: Bearer {token}
Content-Type: application/json

{
  "amount": 100.00,
  "paymentMethod": "ALIPAY"
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "充值订单已创建，请完成支付",
  "data": {
    "orderNo": "PAY20260106100000A1B2C3D4",
    "paymentMethod": "ALIPAY",
    "amount": 100.00,
    "status": 1,
    "paymentUrl": "mock://alipay/pay?orderNo=PAY20260106100000A1B2C3D4",
    "thirdPartyOrderNo": "ALIPAY_abc123def456",
    "message": "请在支付宝中完成支付（模拟）"
  }
}
```

#### 步骤2：模拟支付回调（开发测试用）

```http
POST /payment/mock/callback/{orderNo}?success=true
```

**说明：**
- `success=true` - 模拟支付成功
- `success=false` - 模拟支付失败

**响应示例：**
```json
{
  "code": 200,
  "message": "模拟回调成功",
  "data": null
}
```

支付成功后，钱包余额会自动增加。

### 2. 物业费缴纳（新流程）

#### 方式A：钱包支付（直接扣款）

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

钱包支付会立即扣款，无需额外步骤。

#### 方式B：支付宝/微信支付

**步骤1：创建支付订单**

```http
POST /payment/create-property-fee-order
Authorization: Bearer {token}
Content-Type: application/json

{
  "billId": 1,
  "amount": 680.00,
  "paymentMethod": "ALIPAY"
}
```

**步骤2：发起支付**

```http
POST /payment/initiate/{orderNo}
```

**步骤3：模拟支付回调**

```http
POST /payment/mock/callback/{orderNo}?success=true
```

### 3. 查询订单状态

```http
GET /payment/query/{orderNo}
Authorization: Bearer {token}
```

**响应示例：**
```json
{
  "code": 200,
  "data": {
    "orderId": 1,
    "orderNo": "PAY20260106100000A1B2C3D4",
    "userId": 10,
    "orderType": "RECHARGE",
    "amount": 100.00,
    "paymentMethod": "ALIPAY",
    "status": 2,
    "thirdPartyOrderNo": "ALIPAY_abc123def456",
    "callbackTime": "2026-01-06 10:05:00",
    "expireTime": "2026-01-06 10:30:00",
    "createTime": "2026-01-06 10:00:00"
  }
}
```

**订单状态说明：**
- `0` - 待支付
- `1` - 支付中
- `2` - 支付成功
- `3` - 支付失败
- `4` - 已取消

### 4. 取消订单

```http
POST /payment/cancel/{orderNo}
Authorization: Bearer {token}
```

**注意：**
- 只能取消待支付或支付中的订单
- 已支付成功的订单无法取消

## 完整使用流程示例

### 场景1：用户充值100元（支付宝支付）

```bash
# 1. 创建充值订单
curl -X POST http://localhost:8080/wallet/recharge \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 100.00,
    "paymentMethod": "ALIPAY"
  }'

# 响应获得 orderNo: PAY20260106100000A1B2C3D4

# 2. 模拟支付成功
curl -X POST "http://localhost:8080/payment/mock/callback/PAY20260106100000A1B2C3D4?success=true"

# 3. 查看钱包余额
curl -X GET http://localhost:8080/wallet/info \
  -H "Authorization: Bearer {token}"
```

### 场景2：缴纳物业费（微信支付）

```bash
# 1. 查看物业费账单
curl -X GET "http://localhost:8080/property-fee/bills?status=0" \
  -H "Authorization: Bearer {token}"

# 2. 使用钱包支付（推荐）
curl -X POST http://localhost:8080/property-fee/pay \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "billId": 1,
    "amount": 680.00,
    "paymentMethod": "WALLET"
  }'
```

## 支付方式对比

| 支付方式 | 特点 | 适用场景 | 是否需要回调 |
|---------|------|---------|------------|
| **WALLET（钱包）** | 即时到账，无需回调 | 小额支付 | ❌ 否 |
| **ALIPAY（支付宝）** | 需要模拟回调 | 大额充值 | ✅ 是 |
| **WECHAT（微信）** | 需要模拟回调 | 大额充值 | ✅ 是 |

## 开发测试建议

### 1. 测试充值流程

```javascript
// 测试脚本示例
async function testRecharge() {
  // 创建充值订单
  const rechargeRes = await fetch('/wallet/recharge', {
    method: 'POST',
    headers: {
      'Authorization': 'Bearer ' + token,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      amount: 100,
      paymentMethod: 'ALIPAY'
    })
  });
  
  const { data } = await rechargeRes.json();
  console.log('订单号:', data.orderNo);
  
  // 模拟支付成功
  await fetch(`/payment/mock/callback/${data.orderNo}?success=true`, {
    method: 'POST'
  });
  
  console.log('充值成功！');
}
```

### 2. 测试支付失败

```bash
# 模拟支付失败
curl -X POST "http://localhost:8080/payment/mock/callback/{orderNo}?success=false"
```

### 3. 测试订单过期

创建订单后等待30分钟，订单会自动过期。

## 注意事项

⚠️ **重要提示：**

1. **模拟环境** - 这是模拟支付系统，仅用于开发测试
2. **生产环境** - 生产环境需要集成真实的支付宝/微信SDK
3. **订单过期** - 订单30分钟后自动过期
4. **幂等性** - 支付回调已做幂等处理，重复回调不会重复扣款
5. **并发安全** - 使用数据库行锁保证并发安全

## 数据表说明

### payment_order（支付订单表）

| 字段 | 类型 | 说明 |
|-----|------|------|
| order_id | BIGINT | 订单ID |
| order_no | VARCHAR(64) | 订单号（唯一） |
| user_id | BIGINT | 用户ID |
| order_type | VARCHAR(32) | 订单类型 |
| amount | DECIMAL(10,2) | 订单金额 |
| payment_method | VARCHAR(32) | 支付方式 |
| status | TINYINT | 订单状态 |
| third_party_order_no | VARCHAR(128) | 第三方订单号 |
| expire_time | DATETIME | 过期时间 |

### mock_payment_record（模拟支付记录表）

| 字段 | 类型 | 说明 |
|-----|------|------|
| record_id | BIGINT | 记录ID |
| order_no | VARCHAR(64) | 订单号 |
| payment_method | VARCHAR(32) | 支付方式 |
| amount | DECIMAL(10,2) | 支付金额 |
| mock_result | TINYINT | 模拟结果 |
| mock_message | VARCHAR(255) | 模拟消息 |

## 常见问题

### Q1: 充值后余额没有变化？

检查支付订单状态，确保已调用模拟回调接口。

### Q2: 如何查看支付记录？

```http
GET /wallet/transactions?type=RECHARGE
```

### Q3: 订单过期了怎么办？

重新创建充值订单即可。

### Q4: 可以取消已支付的订单吗？

不可以，已支付成功的订单无法取消。

## 下一步优化建议

1. **真实支付集成** - 集成支付宝和微信支付SDK
2. **支付通知** - 添加支付成功的消息通知
3. **退款功能** - 实现订单退款功能
4. **对账功能** - 添加支付对账功能
5. **支付限额** - 添加单笔和单日支付限额

---

如有问题，请查阅项目文档或联系开发团队。
