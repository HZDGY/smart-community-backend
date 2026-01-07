# 订单系统API测试文档

## 准备工作

### 1. 初始化数据库
```bash
# 执行SQL文件
mysql -u root -p smart_community < unified_order_system.sql
```

### 2. 启动后端服务
```bash
mvn spring-boot:run
```

### 3. 获取用户Token
```bash
# 登录获取token
curl -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "18800000001",
    "password": "123456"
  }'

# 保存返回的token，后续请求需要携带
export TOKEN="your_token_here"
```

## API测试

### 测试1: 添加商品到购物车

```bash
curl -X POST http://localhost:8080/api/mall/shopping-cart/add \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "storeId": 1,
    "quantity": 2
  }'
```

### 测试2: 查看购物车

```bash
curl -X GET http://localhost:8080/api/mall/shopping-cart/list \
  -H "Authorization: Bearer $TOKEN"
```

### 测试3: 创建商品订单

```bash
curl -X POST http://localhost:8080/api/orders/product/create \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "storeId": 1,
    "cartItemIds": [1, 2],
    "remark": "尽快配送"
  }'
```

**预期响应:**
```json
{
  "code": 200,
  "message": "订单创建成功",
  "data": {
    "orderId": 100,
    "orderNo": "ORDER20260106XXXXXX",
    "orderType": "PRODUCT",
    "orderTypeDesc": "商品订单",
    "amount": 199.98,
    "status": 0,
    "statusDesc": "待支付",
    "products": [...]
  }
}
```

### 测试4: 查询所有订单

```bash
curl -X GET "http://localhost:8080/api/orders/list?pageNum=1&pageSize=10" \
  -H "Authorization: Bearer $TOKEN"
```

### 测试5: 查询待支付订单

```bash
curl -X GET "http://localhost:8080/api/orders/list/by-status?status=0&pageNum=1&pageSize=10" \
  -H "Authorization: Bearer $TOKEN"
```

### 测试6: 查询商品订单

```bash
curl -X GET "http://localhost:8080/api/orders/list/by-type?orderType=PRODUCT&pageNum=1&pageSize=10" \
  -H "Authorization: Bearer $TOKEN"
```

### 测试7: 查询订单详情

```bash
# 替换 {orderId} 为实际的订单ID
curl -X GET http://localhost:8080/api/orders/100 \
  -H "Authorization: Bearer $TOKEN"
```

### 测试8: 发起支付

```bash
# 替换 {orderNo} 为实际的订单号
curl -X POST http://localhost:8080/api/payment/initiate/ORDER20260106XXXXXX \
  -H "Authorization: Bearer $TOKEN"
```

### 测试9: 模拟支付成功

```bash
curl -X POST "http://localhost:8080/api/payment/mock/callback/ORDER20260106XXXXXX?success=true" \
  -H "Authorization: Bearer $TOKEN"
```

### 测试10: 确认收货

```bash
curl -X POST http://localhost:8080/api/orders/100/confirm \
  -H "Authorization: Bearer $TOKEN"
```

### 测试11: 取消订单

```bash
# 只能取消待支付的订单
curl -X POST http://localhost:8080/api/orders/100/cancel \
  -H "Authorization: Bearer $TOKEN"
```

### 测试12: 查询已完成订单

```bash
curl -X GET "http://localhost:8080/api/orders/list/by-status?status=3&pageNum=1&pageSize=10" \
  -H "Authorization: Bearer $TOKEN"
```

## 完整流程测试

### 场景1: 用户购买商品完整流程

```bash
# 1. 添加商品到购物车
curl -X POST http://localhost:8080/api/mall/shopping-cart/add \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "storeId": 1, "quantity": 2}'

# 2. 创建订单（记下返回的orderNo）
curl -X POST http://localhost:8080/api/orders/product/create \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"storeId": 1, "cartItemIds": [1], "remark": "测试订单"}'

# 3. 发起支付
curl -X POST http://localhost:8080/api/payment/initiate/ORDER20260106XXXXXX \
  -H "Authorization: Bearer $TOKEN"

# 4. 模拟支付成功
curl -X POST "http://localhost:8080/api/payment/mock/callback/ORDER20260106XXXXXX?success=true" \
  -H "Authorization: Bearer $TOKEN"

# 5. 查询订单（状态应该是2-待取货）
curl -X GET http://localhost:8080/api/orders/100 \
  -H "Authorization: Bearer $TOKEN"

# 6. 确认收货
curl -X POST http://localhost:8080/api/orders/100/confirm \
  -H "Authorization: Bearer $TOKEN"

# 7. 查询订单（状态应该是3-已完成）
curl -X GET http://localhost:8080/api/orders/100 \
  -H "Authorization: Bearer $TOKEN"
```

### 场景2: 用户取消订单流程

```bash
# 1. 创建订单
curl -X POST http://localhost:8080/api/orders/product/create \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"storeId": 1, "cartItemIds": [2], "remark": "测试取消"}'

# 2. 查询待支付订单
curl -X GET "http://localhost:8080/api/orders/list/by-status?status=0" \
  -H "Authorization: Bearer $TOKEN"

# 3. 取消订单
curl -X POST http://localhost:8080/api/orders/101/cancel \
  -H "Authorization: Bearer $TOKEN"

# 4. 查询已取消订单
curl -X GET "http://localhost:8080/api/orders/list/by-status?status=5" \
  -H "Authorization: Bearer $TOKEN"
```

## 使用Postman测试

### 导入环境变量

```json
{
  "name": "Smart Community",
  "values": [
    {
      "key": "baseUrl",
      "value": "http://localhost:8080/api",
      "enabled": true
    },
    {
      "key": "token",
      "value": "",
      "enabled": true
    }
  ]
}
```

### 请求示例

**URL:** `{{baseUrl}}/orders/list`

**Headers:**
```
Authorization: Bearer {{token}}
```

## 预期结果验证

### 1. 创建订单成功
- ✅ 返回订单详情
- ✅ 订单状态为 0（待支付）
- ✅ 购物车商品被清空
- ✅ 商品库存减少

### 2. 查询订单成功
- ✅ 返回订单列表
- ✅ 支持分页
- ✅ 可按状态筛选
- ✅ 可按类型筛选

### 3. 支付成功
- ✅ 订单状态变为 2（待取货）
- ✅ 记录支付时间

### 4. 确认收货成功
- ✅ 订单状态变为 3（已完成）
- ✅ 记录完成时间

### 5. 取消订单成功
- ✅ 订单状态变为 5（已取消）
- ✅ 商品库存恢复

## 常见问题

### 1. Token过期
```
重新登录获取新token
```

### 2. 购物车为空
```
先添加商品到购物车
```

### 3. 商品库存不足
```
检查商品库存，增加库存或减少购买数量
```

### 4. 订单不存在
```
确认订单ID是否正确
确认是否有权限查看该订单
```

## Swagger UI测试

访问: http://localhost:8080/doc.html

在 Swagger UI 中可以直接测试所有接口，更加直观。

