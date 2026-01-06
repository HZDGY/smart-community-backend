# 支付接口使用说明（重要更新）

## 🔄 接口调整说明

为了更符合实际支付流程，我们将创建订单和发起支付分为两个步骤。

## 📋 两种使用方式

### 方式1：分步操作（标准流程）

#### 步骤1：创建订单
```bash
POST /payment/create-order
```

**请求示例：**
```json
{
  "orderType": "RECHARGE",
  "amount": 100.00,
  "paymentMethod": "ALIPAY",
  "description": "钱包充值"
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "订单创建成功，请调用发起支付接口",
  "data": {
    "orderId": 1,
    "orderNo": "ORDER20260106110000A1B2C3D4",
    "userId": 10,
    "orderType": "RECHARGE",
    "amount": 100.00,
    "paymentMethod": "ALIPAY",
    "status": 0,
    "expireTime": "2026-01-06 11:30:00",
    "createTime": "2026-01-06 11:00:00"
  }
}
```

**注意：** 此时订单状态为 `0`（待支付）

#### 步骤2：发起支付
```bash
POST /payment/initiate/{orderNo}
```

**请求示例：**
```bash
curl -X POST http://localhost:8080/payment/initiate/ORDER20260106110000A1B2C3D4
```

**响应示例：**
```json
{
  "code": 200,
  "data": {
    "orderNo": "ORDER20260106110000A1B2C3D4",
    "paymentMethod": "ALIPAY",
    "amount": 100.00,
    "status": 1,
    "paymentUrl": "mock://alipay/pay?orderNo=ORDER20260106110000A1B2C3D4",
    "thirdPartyOrderNo": "ALIPAY_abc123def456",
    "message": "请在支付宝中完成支付（模拟）"
  }
}
```

**注意：** 此时订单状态变为 `1`（支付中）

#### 步骤3：模拟支付回调
```bash
POST /payment/mock/callback/{orderNo}?success=true
```

### 方式2：一步完成（便捷接口）

```bash
POST /payment/create-and-pay
```

**请求示例：**
```json
{
  "orderType": "RECHARGE",
  "amount": 100.00,
  "paymentMethod": "ALIPAY",
  "description": "钱包充值"
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "订单创建成功，请完成支付",
  "data": {
    "orderNo": "ORDER20260106110100B2C3D4E5",
    "paymentMethod": "ALIPAY",
    "amount": 100.00,
    "status": 1,
    "paymentUrl": "mock://alipay/pay?orderNo=ORDER20260106110100B2C3D4E5",
    "thirdPartyOrderNo": "ALIPAY_def456ghi789",
    "message": "请在支付宝中完成支付（模拟）"
  }
}
```

**注意：** 这个接口会自动完成创建订单和发起支付两个步骤

## 🆚 两种方式对比

| 特性 | 分步操作 | 一步完成 |
|------|---------|---------|
| **接口数量** | 2个 | 1个 |
| **灵活性** | 高 | 低 |
| **适用场景** | 需要在创建订单后做其他操作 | 简单快速支付 |
| **推荐度** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |

## 💡 完整示例

### 示例1：使用分步操作

```javascript
// 步骤1：创建订单
const createRes = await fetch('/payment/create-order', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer ' + token,
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    orderType: 'RECHARGE',
    amount: 100,
    paymentMethod: 'ALIPAY',
    description: '充值100元'
  })
});

const { data: order } = await createRes.json();
console.log('订单号:', order.orderNo);
console.log('订单状态:', order.status); // 0 = 待支付

// 步骤2：发起支付
const payRes = await fetch(`/payment/initiate/${order.orderNo}`, {
  method: 'POST'
});

const { data: payment } = await payRes.json();
console.log('支付URL:', payment.paymentUrl);
console.log('订单状态:', payment.status); // 1 = 支付中

// 步骤3：模拟支付成功
await fetch(`/payment/mock/callback/${order.orderNo}?success=true`, {
  method: 'POST'
});

// 步骤4：查询订单状态
const statusRes = await fetch(`/payment/query/${order.orderNo}`);
const { data: finalOrder } = await statusRes.json();
console.log('最终状态:', finalOrder.status); // 2 = 支付成功
```

### 示例2：使用一步完成

```javascript
// 一步完成创建和发起支付
const res = await fetch('/payment/create-and-pay', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer ' + token,
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    orderType: 'RECHARGE',
    amount: 100,
    paymentMethod: 'ALIPAY',
    description: '充值100元'
  })
});

const { data: payment } = await res.json();
console.log('订单号:', payment.orderNo);
console.log('支付URL:', payment.paymentUrl);

// 模拟支付成功
await fetch(`/payment/mock/callback/${payment.orderNo}?success=true`, {
  method: 'POST'
});
```

## 📊 订单状态流转

### 分步操作流程
```
创建订单 (status=0 待支付)
    ↓
发起支付 (status=1 支付中)
    ↓
支付回调 (status=2 支付成功 / status=3 支付失败)
```

### 一步完成流程
```
创建并发起支付 (status=1 支付中)
    ↓
支付回调 (status=2 支付成功 / status=3 支付失败)
```

## ⚠️ 重要提示

1. **创建订单接口** (`/create-order`) 只创建订单，不发起支付
2. **发起支付接口** (`/initiate/{orderNo}`) 只能对待支付状态的订单使用
3. **便捷接口** (`/create-and-pay`) 适合简单场景
4. **订单过期**：创建后30分钟未支付会自动过期

## 🔧 错误处理

### 常见错误1：订单状态不正确

**错误信息：**
```json
{
  "code": 500,
  "message": "订单状态不正确"
}
```

**原因：** 对已经发起支付的订单再次调用发起支付接口

**解决：** 
- 查询订单状态：`GET /payment/query/{orderNo}`
- 如果状态是待支付(0)，可以发起支付
- 如果状态是支付中(1)，直接进行支付回调
- 如果状态是成功(2)或失败(3)，无需再操作

### 常见错误2：订单已过期

**错误信息：**
```json
{
  "code": 500,
  "message": "订单已过期"
}
```

**解决：** 重新创建订单

## 📚 相关接口汇总

| 接口 | 方法 | 说明 |
|------|------|------|
| /payment/create-order | POST | 创建订单（不发起支付） |
| /payment/create-and-pay | POST | 创建订单并发起支付 |
| /payment/initiate/{orderNo} | POST | 发起支付 |
| /payment/query/{orderNo} | GET | 查询订单状态 |
| /payment/cancel/{orderNo} | POST | 取消订单 |
| /payment/mock/callback/{orderNo} | POST | 模拟支付回调 |

## ✅ 推荐使用方式

### 充值场景
推荐使用 **便捷接口** `/payment/create-and-pay`

### 物业费场景
推荐使用 **分步操作**，先创建订单，确认后再发起支付

### 批量订单场景
推荐使用 **分步操作**，便于订单管理

---

**更新时间：** 2026-01-06 11:05  
**版本：** v1.1
