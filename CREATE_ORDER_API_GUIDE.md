# 通用创建订单接口说明

## 📋 接口信息

**接口地址：** `POST /payment/create-order`

**接口说明：** 创建支付订单的通用接口，支持充值和物业费缴纳

## 🚀 使用方法

### 1. 充值订单

```bash
curl -X POST http://localhost:8080/payment/create-order \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "orderType": "RECHARGE",
    "amount": 100.00,
    "paymentMethod": "ALIPAY",
    "description": "钱包充值"
  }'
```

**响应示例：**
```json
{
  "code": 200,
  "message": "订单创建成功",
  "data": {
    "orderNo": "ORDER20260106105000A1B2C3D4",
    "paymentMethod": "ALIPAY",
    "amount": 100.00,
    "status": 1,
    "paymentUrl": "mock://alipay/pay?orderNo=ORDER20260106105000A1B2C3D4",
    "thirdPartyOrderNo": "ALIPAY_abc123def456",
    "message": "请在支付宝中完成支付（模拟）"
  }
}
```

### 2. 物业费缴纳订单

```bash
curl -X POST http://localhost:8080/payment/create-order \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "orderType": "PROPERTY_FEE",
    "amount": 680.00,
    "paymentMethod": "WECHAT",
    "relatedId": 1,
    "description": "缴纳2026年1月物业费"
  }'
```

**响应示例：**
```json
{
  "code": 200,
  "message": "订单创建成功",
  "data": {
    "orderNo": "ORDER20260106105100B2C3D4E5",
    "paymentMethod": "WECHAT",
    "amount": 680.00,
    "status": 1,
    "paymentUrl": "mock://wechat/pay?orderNo=ORDER20260106105100B2C3D4E5",
    "thirdPartyOrderNo": "WECHAT_def456ghi789",
    "message": "请在微信中完成支付（模拟）"
  }
}
```

## 📝 请求参数说明

| 参数 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| orderType | String | 是 | 订单类型 | RECHARGE / PROPERTY_FEE |
| amount | BigDecimal | 是 | 订单金额 | 100.00 |
| paymentMethod | String | 是 | 支付方式 | ALIPAY / WECHAT / WALLET |
| relatedId | Long | 否 | 关联业务ID | 1（物业费账单ID） |
| description | String | 否 | 订单描述 | 钱包充值 |

### 订单类型（orderType）

- **RECHARGE** - 充值订单
- **PROPERTY_FEE** - 物业费缴纳订单

### 支付方式（paymentMethod）

- **ALIPAY** - 支付宝支付
- **WECHAT** - 微信支付
- **WALLET** - 钱包支付

## 🔄 完整支付流程

### 步骤1：创建订单

```javascript
const response = await fetch('/payment/create-order', {
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

const { data } = await response.json();
console.log('订单号:', data.orderNo);
console.log('支付URL:', data.paymentUrl);
```

### 步骤2：模拟支付成功

```javascript
await fetch(`/payment/mock/callback/${data.orderNo}?success=true`, {
  method: 'POST'
});
```

### 步骤3：查询订单状态

```javascript
const statusRes = await fetch(`/payment/query/${data.orderNo}`);
const order = await statusRes.json();
console.log('订单状态:', order.data.status);
// 2 = 支付成功
```

## 💡 使用场景

### 场景1：用户充值

```json
{
  "orderType": "RECHARGE",
  "amount": 100.00,
  "paymentMethod": "ALIPAY",
  "description": "钱包充值"
}
```

充值成功后，钱包余额会自动增加。

### 场景2：缴纳物业费

```json
{
  "orderType": "PROPERTY_FEE",
  "amount": 680.00,
  "paymentMethod": "WECHAT",
  "relatedId": 1,
  "description": "缴纳物业费"
}
```

**注意：** 
- `relatedId` 为物业费账单ID
- 支付成功后，账单状态会自动更新

### 场景3：钱包支付

```json
{
  "orderType": "RECHARGE",
  "amount": 50.00,
  "paymentMethod": "WALLET",
  "description": "钱包充值"
}
```

**注意：** 钱包支付会立即扣款，无需模拟回调。

## ⚠️ 注意事项

1. **订单过期时间**：订单创建后30分钟内有效
2. **支付方式限制**：
   - 充值订单：支持所有支付方式
   - 物业费订单：建议使用WALLET（钱包）直接扣款
3. **金额限制**：
   - 最小金额：0.01元
   - 最大金额：无限制（建议设置合理上限）
4. **关联ID**：
   - 充值订单：relatedId可以为空
   - 物业费订单：relatedId必须填写账单ID

## 🆚 与原接口的区别

### 原充值接口（仍可使用）

```
POST /wallet/recharge
```

**特点：**
- 专门用于充值
- 自动设置orderType为RECHARGE
- 参数更简洁

### 新通用接口（推荐）

```
POST /payment/create-order
```

**特点：**
- 支持多种订单类型
- 更灵活的参数配置
- 统一的订单管理

## 📚 相关接口

| 接口 | 说明 |
|------|------|
| POST /payment/create-order | 创建订单（通用） |
| POST /payment/initiate/{orderNo} | 发起支付 |
| GET /payment/query/{orderNo} | 查询订单状态 |
| POST /payment/cancel/{orderNo} | 取消订单 |
| POST /payment/mock/callback/{orderNo} | 模拟支付回调 |

## 🔍 错误处理

### 常见错误

**1. 金额必须大于0**
```json
{
  "code": 500,
  "message": "支付金额必须大于0"
}
```

**2. 订单类型错误**
```json
{
  "code": 400,
  "message": "订单类型不能为空"
}
```

**3. 支付方式错误**
```json
{
  "code": 400,
  "message": "支付方式不能为空"
}
```

## ✅ 最佳实践

1. **充值场景**：使用 `/wallet/recharge` 接口更简洁
2. **物业费场景**：使用 `/payment/create-order` 接口更灵活
3. **批量订单**：使用通用接口，便于统一管理
4. **错误处理**：始终检查响应状态码和错误信息

---

**更新时间：** 2026-01-06  
**接口版本：** v1.0
