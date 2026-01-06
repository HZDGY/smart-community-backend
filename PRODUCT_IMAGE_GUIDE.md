# 商品多图片功能使用指南

## 功能概述

商品多图片功能允许为每个商品上传多张图片（最多10张），支持设置主图、调整排序等操作。

## 快速开始

### 1. 初始化数据库

执行数据库脚本：

```bash
mysql -u root -p your_database < product_image_init.sql
```

### 2. 重启项目

```bash
mvn spring-boot:run
```

### 3. 访问 API 文档

打开 `http://localhost:8080/doc.html` 查看商品图片管理接口。

## API 使用说明

### 1. 上传商品图片

```http
POST /product/{productId}/images
Content-Type: multipart/form-data

file: [图片文件]
isMain: true/false (可选)
```

**示例（使用 curl）**：
```bash
curl -X POST "http://localhost:8080/product/1/images" \
  -F "file=@/path/to/image.jpg" \
  -F "isMain=true"
```

**响应**：
```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "imageId": 1,
    "productId": 1,
    "imageUrl": "http://localhost:8080/uploads/products/20260105/abc123.jpg",
    "isMain": 1,
    "sortOrder": 0,
    "createTime": "2026-01-05 14:00:00"
  }
}
```

### 2. 获取商品图片列表

```http
GET /product/{productId}/images
```

**响应**：
```json
{
  "code": 200,
  "data": [
    {
      "imageId": 1,
      "productId": 1,
      "imageUrl": "http://localhost:8080/uploads/products/20260105/abc123.jpg",
      "isMain": 1,
      "sortOrder": 0
    },
    {
      "imageId": 2,
      "productId": 1,
      "imageUrl": "http://localhost:8080/uploads/products/20260105/def456.jpg",
      "isMain": 0,
      "sortOrder": 1
    }
  ]
}
```

### 3. 设置主图

```http
PUT /product/images/{imageId}/set-main
```

**说明**：将指定图片设置为主图，同时取消该商品其他图片的主图状态，并更新商品表的 `cover_img` 字段。

### 4. 删除图片

```http
DELETE /product/images/{imageId}
```

**说明**：
- 删除图片记录和物理文件
- 如果删除的是主图，自动将第一张图片设为主图
- 如果删除后没有图片了，清空商品的封面图

### 5. 调整图片排序

```http
PUT /product/{productId}/images/sort
Content-Type: application/json

{
  "imageOrders": [
    {"imageId": 1, "sortOrder": 0},
    {"imageId": 2, "sortOrder": 1},
    {"imageId": 3, "sortOrder": 2}
  ]
}
```

### 6. 获取图片详情

```http
GET /product/images/{imageId}
```

## 使用场景

### 场景 1：新商品上传图片

```bash
# 1. 上传第一张图片（自动设为主图）
POST /product/1/images
file: product_main.jpg

# 2. 上传更多图片
POST /product/1/images
file: product_detail_1.jpg

POST /product/1/images
file: product_detail_2.jpg

# 3. 查看所有图片
GET /product/1/images
```

### 场景 2：更换主图

```bash
# 1. 查看当前所有图片
GET /product/1/images

# 2. 将图片ID为3的设为主图
PUT /product/images/3/set-main
```

### 场景 3：调整图片顺序

```bash
# 将图片重新排序
PUT /product/1/images/sort
{
  "imageOrders": [
    {"imageId": 3, "sortOrder": 0},  # 原来的第3张移到第1位
    {"imageId": 1, "sortOrder": 1},  # 原来的第1张移到第2位
    {"imageId": 2, "sortOrder": 2}   # 原来的第2张移到第3位
  ]
}
```

## 业务规则

### 1. 图片数量限制
- 每个商品最多 **10 张**图片
- 超过限制会返回错误提示

### 2. 文件格式限制
- 支持格式：jpg、jpeg、png、gif、webp
- 文件大小：最大 10MB

### 3. 主图规则
- 每个商品只能有 **一张主图**
- 上传第一张图片时自动设为主图
- 设置新主图时，自动取消其他图片的主图状态
- 删除主图时，自动将第一张图片设为主图

### 4. 排序规则
- `sortOrder` 数字越小越靠前
- 新上传的图片默认排在最后
- 可以通过排序接口调整顺序

### 5. 删除规则
- 删除图片时同时删除物理文件
- 删除主图后自动设置新主图
- 删除所有图片后，商品封面图清空

## 数据库字段说明

### product_image 表

| 字段 | 类型 | 说明 |
|------|------|------|
| image_id | BIGINT | 图片ID（主键） |
| product_id | BIGINT | 商品ID |
| image_url | VARCHAR(500) | 图片URL |
| is_main | TINYINT | 是否主图（0-否 1-是） |
| sort_order | INT | 排序顺序 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

## 文件存储

### 存储路径
```
uploads/products/yyyyMMdd/uuid.jpg
```

### 示例
```
uploads/products/20260105/a1b2c3d4e5f6.jpg
```

### 访问 URL
```
http://localhost:8080/uploads/products/20260105/a1b2c3d4e5f6.jpg
```

## 注意事项

1. **并发安全**：设置主图和删除操作使用事务保证数据一致性
2. **文件清理**：删除图片时会同时删除物理文件
3. **主图同步**：设置主图时会自动更新 `product` 表的 `cover_img` 字段
4. **错误处理**：上传失败时会抛出异常，不会产生脏数据

## 常见问题

### Q1: 上传图片失败？

**可能原因**：
- 文件格式不支持
- 文件大小超过限制
- 商品不存在
- 已达到图片数量上限

**解决方法**：检查文件格式和大小，确认商品ID正确。

### Q2: 如何批量上传图片？

可以循环调用上传接口，每次上传一张图片。

### Q3: 删除主图后会怎样？

系统会自动将 `sortOrder` 最小的图片设为新主图。如果没有其他图片，商品的 `cover_img` 会被清空。

### Q4: 图片排序如何工作？

`sortOrder` 字段控制显示顺序，数字越小越靠前。可以通过排序接口批量更新。

## 最佳实践

1. **上传顺序**：先上传主图，再上传详情图
2. **图片命名**：使用有意义的文件名，便于管理
3. **图片优化**：上传前压缩图片，提高加载速度
4. **定期清理**：删除不用的商品时，记得删除关联图片

---

如有任何问题，请查阅项目文档或联系开发团队。
