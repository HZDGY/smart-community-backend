# 支付接口完整对比说明

## 📋 所有支付相关接口

### 1. 充值专用接口（便捷）

```
POST /wallet/recharge
```

**特点：**
- ✅ 专门用于充值
- ✅ 一步完成（创建订单 + 发起支付）
- ✅ 参数简洁
- ✅ 推荐用于充值场景

**请求示例：**
```json
{
  "amount": 100.00,
  "paymentMethod": "ALIPAY"
}
```

**响应：** 返回 `PaymentResponse`（包含支付URL）

---

### 2. 通用创建订单接口（分步）

```
POST /payment/create-order
```

**特点：**
- ✅ 支持所有订单类型
- ✅ 只创建订单，不发起支付
- ✅ 需要手动调用发起支付接口
- ✅ 适合需要确认的场景

**请求示例：**
```json
{
  "orderType": "RECHARGE",
  "amount": 100.00,
  "paymentMethod": "ALIPAY",
  "description": "钱包充值"
}
```

**响应：** 返回 `PaymentOrder`（订单信息，状态=0待支付）

**后续步骤：** 需要调用 `POST /payment/initiate/{orderNo}` 发起支付

---

### 3. 通用创建并支付接口（便捷）

```
POST /payment/create-and-pay
```

**特点：**
- ✅ 支持所有订单类型
- ✅ 一步完成（创建订单 + 发起支付）
- ✅ 功能等同于 `/wallet/recharge`
- ✅ 适合快速支付场景

**请求示例：**
```json
{
  "orderType": "RECHARGE",
  "amount": 100.00,
  "paymentMethod": "ALIPAY",
  "description": "钱包充值"
}
```

**响应：** 返回 `PaymentResponse`（包含支付URL）

---

### 4. 发起支付接口

```
POST /payment/initiate/{orderNo}
```

**特点：**
- ✅ 对已创建的订单发起支付
- ✅ 只能用于待支付状态的订单
- ✅ 配合 `/payment/create-order` 使用

**响应：** 返回 `PaymentResponse`（包含支付URL）

---

### 5. 查询订单状态

```
GET /payment/query/{orderNo}
```

**响应：** 返回 `PaymentOrder`（完整订单信息）

---

### 6. 取消订单

```
POST /payment/cancel/{orderNo}
```

**限制：** 只能取消待支付或支付中的订单

---

### 7. 模拟支付回调

```
POST /payment/mock/callback/{orderNo}?success=true
```

**参数：**
- `success=true` - 模拟支付成功
- `success=false` - 模拟支付失败

---

## 🎯 使用场景推荐

### 场景1：用户充值（推荐方式）

**方式A：使用充值专用接口（最简单）**
```bash
# 一步完成
POST /wallet/recharge
{
  "amount": 100,
  "paymentMethod": "ALIPAY"
}

# 模拟支付成功
POST /payment/mock/callback/{orderNo}?success=true
```

**方式B：使用通用便捷接口**
```bash
# 一步完成
POST /payment/create-and-pay
{
  "orderType": "RECHARGE",
  "amount": 100,
  "paymentMethod": "ALIPAY"
}

# 模拟支付成功
POST /payment/mock/callback/{orderNo}?success=true
```

**方式C：使用分步操作**
```bash
# 步骤1：创建订单
POST /payment/create-order
{
  "orderType": "RECHARGE",
  "amount": 100,
  "paymentMethod": "ALIPAY"
}

# 步骤2：发起支付
POST /payment/initiate/{orderNo}

# 步骤3：模拟支付成功
POST /payment/mock/callback/{orderNo}?success=true
```

**推荐：** 方式A（最简单）

---

### 场景2：缴纳物业费

**推荐使用分步操作：**
```bash
# 步骤1：创建订单（用户可以先查看订单详情）
POST /payment/create-order
{
  "orderType": "PROPERTY_FEE",
  "amount": 680,
  "paymentMethod": "WECHAT",
  "relatedId": 1,
  "description": "缴纳2026年1月物业费"
}

# 步骤2：用户确认后发起支付
POST /payment/initiate/{orderNo}

# 步骤3：模拟支付成功
POST /payment/mock/callback/{orderNo}?success=true
```

---

## 📊 接口对比表

| 接口 | 订单类型 | 步骤 | 返回类型 | 推荐场景 |
|------|---------|------|---------|---------|
| POST /wallet/recharge | 仅充值 | 1步 | PaymentResponse | 充值 ⭐⭐⭐⭐⭐ |
| POST /payment/create-and-pay | 所有 | 1步 | PaymentResponse | 快速支付 ⭐⭐⭐⭐ |
| POST /payment/create-order | 所有 | 2步 | PaymentOrder | 需要确认 ⭐⭐⭐⭐⭐ |
| POST /payment/initiate/{orderNo} | - | - | PaymentResponse | 配合create-order |

---

## 💡 最佳实践

### 1. 充值场景
```javascript
// 推荐：使用充值专用接口
const res = await fetch('/wallet/recharge', {
  method: 'POST',
  body: JSON.stringify({
    amount: 100,
    paymentMethod: 'ALIPAY'
  })
});

const { data } = await res.json();
// 模拟支付
await fetch(`/payment/mock/callback/${data.orderNo}?success=true`, {
  method: 'POST'
});
```

### 2. 物业费场景
```javascript
// 推荐：使用分步操作
// 步骤1：创建订单
const createRes = await fetch('/payment/create-order', {
  method: 'POST',
  body: JSON.stringify({
    orderType: 'PROPERTY_FEE',
    amount: 680,
    paymentMethod: 'WECHAT',
    relatedId: 1
  })
});

const { data: order } = await createRes.json();

// 步骤2：用户确认后发起支付
const payRes = await fetch(`/payment/initiate/${order.orderNo}`, {
  method: 'POST'
});

// 步骤3：模拟支付
await fetch(`/payment/mock/callback/${order.orderNo}?success=true`, {
  method: 'POST'
});
```

---

## ⚠️ 注意事项

1. **充值接口** (`/wallet/recharge`) 等同于 `/payment/create-and-pay` + `orderType=RECHARGE`
2. **分步操作** 更灵活，适合需要用户确认的场景
3. **便捷接口** 更简单，适合快速支付场景
4. **订单状态**：
   - 创建订单后：status = 0（待支付）
   - 发起支付后：status = 1（支付中）
   - 支付成功后：status = 2（支付成功）

---

## 🔄 接口等价关系

```
/wallet/recharge
    ≈
/payment/create-and-pay (orderType=RECHARGE)
    ≈
/payment/create-order + /payment/initiate/{orderNo}
```

---

**更新时间：** 2026-01-06 11:06  
**版本：** v1.2
