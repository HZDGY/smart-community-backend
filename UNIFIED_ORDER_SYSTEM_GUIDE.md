# 统一订单系统使用指南

## 📋 系统概述

本系统将所有订单（商品订单、充值订单、物业费订单等）统一到一个订单表（`payment_order`）中管理，通过 `order_type` 字段区分不同类型的订单。

## 🏗️ 数据库设计

### 核心表

1. **payment_order** - 统一订单表
   - 所有类型的订单都存储在此表
   - 通过 `order_type` 区分订单类型
   - 通过 `status` 管理订单状态

2. **order_product** - 订单商品关联表
   - 仅用于商品订单
   - 记录订单包含的商品信息
   - 通过 `order_id` 关联到 `payment_order`

### 数据库初始化

执行 SQL 文件初始化数据库：
```bash
mysql -u root -p smart_community < unified_order_system.sql
```

## 📊 订单类型 (order_type)

| 类型 | 描述 | 说明 |
|------|------|------|
| PRODUCT | 商品订单 | 用户购买商品的订单，关联 order_product 表 |
| RECHARGE | 钱包充值 | 用户充值钱包 |
| PROPERTY_FEE | 物业费 | 缴纳物业费 |
| PARKING_FEE | 停车费 | 缴纳停车费 |
| SERVICE_FEE | 服务费 | 其他服务费用 |
| OTHER | 其他 | 其他类型订单 |

## 🎯 订单状态 (status)

| 状态码 | 状态名称 | 描述 | 适用场景 |
|--------|----------|------|----------|
| 0 | 待支付 (PENDING) | 订单已创建，等待支付 | 所有订单 |
| 1 | 支付中 (PAYING) | 正在支付中 | 所有订单 |
| 2 | 支付成功/待取货 (SUCCESS) | 支付成功，等待取货 | 商品订单 |
| 3 | 已完成 (COMPLETED) | 已取货/订单完成 | 所有订单 |
| 4 | 支付失败 (FAILED) | 支付失败 | 所有订单 |
| 5 | 已取消 (CANCELLED) | 用户取消订单 | 所有订单 |
| 6 | 已退款 (REFUNDED) | 已退款 | 所有订单 |

## 🔌 API 接口

### 1. 创建商品订单

**接口:** `POST /api/orders/product/create`

**请求体:**
```json
{
  "storeId": 1,
  "cartItemIds": [1, 2, 3],
  "remark": "尽快配送"
}
```

**响应:**
```json
{
  "code": 200,
  "message": "订单创建成功",
  "data": {
    "orderId": 100,
    "orderNo": "ORDER20260106123456ABCD",
    "orderType": "PRODUCT",
    "orderTypeDesc": "商品订单",
    "amount": 299.99,
    "status": 0,
    "statusDesc": "待支付",
    "products": [
      {
        "productId": 1,
        "productName": "商品A",
        "quantity": 2,
        "price": 99.99,
        "subtotal": 199.98
      }
    ]
  }
}
```

### 2. 查询我的所有订单

**接口:** `GET /api/orders/list?pageNum=1&pageSize=10`

**响应:**
```json
{
  "code": 200,
  "data": {
    "records": [
      {
        "orderId": 100,
        "orderNo": "ORDER20260106123456ABCD",
        "orderType": "PRODUCT",
        "orderTypeDesc": "商品订单",
        "amount": 299.99,
        "status": 0,
        "statusDesc": "待支付",
        "storeName": "东软门店",
        "productCount": 3,
        "createTime": "2026-01-06 12:34:56"
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1
  }
}
```

### 3. 按状态查询订单

**接口:** `GET /api/orders/list/by-status?status=0&pageNum=1&pageSize=10`

**参数:**
- `status`: 订单状态 (0-待支付, 2-待取货, 3-已完成等)
- `pageNum`: 页码
- `pageSize`: 每页大小

### 4. 按类型查询订单

**接口:** `GET /api/orders/list/by-type?orderType=PRODUCT&pageNum=1&pageSize=10`

**参数:**
- `orderType`: 订单类型 (PRODUCT, RECHARGE, PROPERTY_FEE等)
- `pageNum`: 页码
- `pageSize`: 每页大小

### 5. 查询订单详情

**接口:** `GET /api/orders/{orderId}`

**响应:**
```json
{
  "code": 200,
  "data": {
    "orderId": 100,
    "orderNo": "ORDER20260106123456ABCD",
    "orderType": "PRODUCT",
    "orderTypeDesc": "商品订单",
    "amount": 299.99,
    "status": 0,
    "statusDesc": "待支付",
    "paymentMethod": "ALIPAY",
    "paymentMethodDesc": "支付宝",
    "storeId": 1,
    "storeName": "东软门店",
    "description": "商品订单",
    "createTime": "2026-01-06 12:34:56",
    "expireTime": "2026-01-06 13:04:56",
    "products": [
      {
        "productId": 1,
        "productName": "商品A",
        "productImage": "http://xxx.jpg",
        "quantity": 2,
        "price": 99.99,
        "subtotal": 199.98
      }
    ]
  }
}
```

### 6. 取消订单

**接口:** `POST /api/orders/{orderId}/cancel`

**响应:**
```json
{
  "code": 200,
  "message": "订单取消成功"
}
```

### 7. 确认收货/完成订单

**接口:** `POST /api/orders/{orderId}/confirm`

**响应:**
```json
{
  "code": 200,
  "message": "确认收货成功"
}
```

## 💡 使用流程

### 商品订单流程

1. **用户加购物车**
   ```
   POST /api/mall/shopping-cart/add
   ```

2. **创建订单**
   ```
   POST /api/orders/product/create
   状态: 0-待支付
   ```

3. **发起支付**
   ```
   POST /api/payment/initiate/{orderNo}
   状态: 1-支付中
   ```

4. **支付完成**
   ```
   模拟支付回调成功
   状态: 2-支付成功/待取货
   ```

5. **用户取货**
   ```
   POST /api/orders/{orderId}/confirm
   状态: 3-已完成
   ```

### 充值订单流程

1. **创建充值订单**
   ```
   POST /api/payment/create-order
   orderType: RECHARGE
   ```

2. **支付完成**
   ```
   自动增加钱包余额
   状态: 3-已完成
   ```

## 🔍 查询订单示例

### 查询待支付订单
```bash
GET /api/orders/list/by-status?status=0
```

### 查询待取货订单
```bash
GET /api/orders/list/by-status?status=2
```

### 查询已完成订单
```bash
GET /api/orders/list/by-status?status=3
```

### 查询所有商品订单
```bash
GET /api/orders/list/by-type?orderType=PRODUCT
```

### 查询所有充值订单
```bash
GET /api/orders/list/by-type?orderType=RECHARGE
```

## ⚠️ 注意事项

1. **订单创建时会：**
   - 校验商品库存
   - 扣减商品库存
   - 清空对应的购物车项
   - 设置30分钟订单过期时间

2. **取消订单时会：**
   - 恢复商品库存（仅商品订单）
   - 只能取消待支付状态的订单

3. **确认收货时：**
   - 只能确认支付成功的订单
   - 订单状态变为已完成

4. **订单查询：**
   - 用户只能查询自己的订单
   - 支持分页查询
   - 支持按状态、类型筛选

## 🎨 前端对接建议

### 订单列表页面布局

```
我的订单
├── Tab切换: 全部 | 待支付 | 待取货 | 已完成
├── 订单列表
│   ├── 订单卡片1
│   │   ├── 订单号: ORDER20260106123456
│   │   ├── 订单类型: 商品订单
│   │   ├── 商品信息（如果是商品订单）
│   │   ├── 订单金额: ¥299.99
│   │   ├── 订单状态: 待支付
│   │   └── 操作按钮: [取消订单] [去支付]
└── 分页组件
```

### 状态对应的操作按钮

| 状态 | 可用操作 |
|------|----------|
| 待支付 (0) | 取消订单、去支付 |
| 支付中 (1) | 查看详情 |
| 待取货 (2) | 确认收货、查看详情 |
| 已完成 (3) | 再次购买、查看详情 |
| 已取消 (5) | 删除订单、查看详情 |

## 🔧 技术实现亮点

1. **统一订单模型**: 所有订单类型使用同一张表，便于管理和查询
2. **中间表设计**: 商品信息通过 order_product 关联，支持一单多品
3. **数据冗余**: 订单商品关联表冗余了商品名称、图片等信息，保证订单历史可查
4. **库存管理**: 创建订单扣减库存，取消订单恢复库存
5. **状态流转**: 清晰的订单状态流转逻辑
6. **类型扩展**: 易于扩展新的订单类型

## 📝 后续优化建议

1. 添加订单搜索功能（按订单号、商品名称）
2. 添加订单退款功能
3. 添加订单评价功能
4. 添加订单物流信息
5. 添加订单导出功能
6. 添加订单统计分析

