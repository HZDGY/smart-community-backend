# 模拟支付系统快速开始

## 🚀 5分钟快速上手

### 步骤1：执行数据库脚本

```bash
mysql -u root -p your_database < payment_system_enhancement.sql
```

### 步骤2：重启项目

```bash
mvn spring-boot:run
```

### 步骤3：测试充值功能

#### 3.1 创建充值订单

```bash
curl -X POST http://localhost:8080/wallet/recharge \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 100.00,
    "paymentMethod": "ALIPAY"
  }'
```

**响应示例：**
```json
{
  "code": 200,
  "message": "充值订单已创建，请完成支付",
  "data": {
    "orderNo": "ORDER20260106101500A1B2C3D4",
    "paymentMethod": "ALIPAY",
    "amount": 100.00,
    "status": 1,
    "paymentUrl": "mock://alipay/pay?orderNo=ORDER20260106101500A1B2C3D4",
    "thirdPartyOrderNo": "ALIPAY_abc123def456",
    "message": "请在支付宝中完成支付（模拟）"
  }
}
```

#### 3.2 模拟支付成功

```bash
curl -X POST "http://localhost:8080/payment/mock/callback/ORDER20260106101500A1B2C3D4?success=true"
```

**响应：**
```json
{
  "code": 200,
  "message": "模拟回调成功",
  "data": null
}
```

#### 3.3 查看钱包余额

```bash
curl -X GET http://localhost:8080/wallet/info \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**响应：**
```json
{
  "code": 200,
  "data": {
    "walletId": 1,
    "userId": 10,
    "balance": 100.00,
    "totalRecharge": 100.00,
    "status": 1
  }
}
```

✅ **充值成功！** 余额已增加100元。

---

## 📱 使用Postman测试

### 1. 导入环境变量

创建环境变量：
- `base_url`: http://localhost:8080
- `token`: 你的JWT token

### 2. 测试充值

**请求：**
```
POST {{base_url}}/wallet/recharge
Headers:
  Authorization: Bearer {{token}}
  Content-Type: application/json

Body:
{
  "amount": 100.00,
  "paymentMethod": "ALIPAY"
}
```

**保存响应中的 orderNo**

### 3. 模拟支付

**请求：**
```
POST {{base_url}}/payment/mock/callback/{{orderNo}}?success=true
```

### 4. 查看结果

**请求：**
```
GET {{base_url}}/wallet/info
Headers:
  Authorization: Bearer {{token}}
```

---

## 🎯 常用场景

### 场景1：充值100元（支付宝）

```javascript
// 1. 创建订单
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

// 2. 模拟支付成功
await fetch(`/payment/mock/callback/${data.orderNo}?success=true`, {
  method: 'POST'
});

console.log('充值成功！');
```

### 场景2：充值失败测试

```bash
# 创建订单
curl -X POST http://localhost:8080/wallet/recharge \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"amount": 50, "paymentMethod": "WECHAT"}'

# 模拟支付失败
curl -X POST "http://localhost:8080/payment/mock/callback/ORDER_NO?success=false"

# 查询订单状态（应该是失败状态）
curl -X GET http://localhost:8080/payment/query/ORDER_NO
```

### 场景3：取消订单

```bash
# 创建订单
curl -X POST http://localhost:8080/wallet/recharge \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"amount": 200, "paymentMethod": "ALIPAY"}'

# 取消订单
curl -X POST http://localhost:8080/payment/cancel/ORDER_NO \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 🔍 API文档

访问 Knife4j 文档查看所有接口：

```
http://localhost:8080/doc.html
```

在文档中可以：
- 查看所有支付相关接口
- 在线测试接口
- 查看请求/响应示例

---

## ❓ 常见问题

### Q: 充值后余额没变化？

**A:** 检查是否调用了模拟支付回调接口。充值流程需要两步：
1. 创建订单
2. 调用回调接口模拟支付成功

### Q: 提示"订单不存在"？

**A:** 检查订单号是否正确，订单号在创建充值订单时返回。

### Q: 如何查看支付记录？

**A:** 
```bash
curl -X GET "http://localhost:8080/wallet/transactions?type=RECHARGE" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Q: 订单过期了怎么办？

**A:** 重新创建充值订单即可，订单30分钟后自动过期。

---

## 📚 更多文档

- **完整使用指南**: `MOCK_PAYMENT_GUIDE.md`
- **开发总结**: `PAYMENT_SYSTEM_SUMMARY.md`
- **钱包系统**: `WALLET_SYSTEM_GUIDE.md`

---

## 🎉 开始使用

现在你已经了解了基本用法，可以开始测试了！

**推荐测试顺序：**
1. ✅ 测试充值（支付宝）
2. ✅ 测试充值（微信）
3. ✅ 测试钱包支付物业费
4. ✅ 测试支付失败
5. ✅ 测试订单取消

祝你使用愉快！🚀
