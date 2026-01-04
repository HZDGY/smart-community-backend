# 报事维修和事项投诉 API 使用指南

## 概述
本文档介绍报事维修和事项投诉功能的API接口使用方法。

## 功能说明

### 1. 报事维修
用户可以选择事项类型，描述问题，提交维修申请至社区管理人员。

**支持的事项类型：**
- 水电维修
- 电梯故障
- 门窗维修
- 网络故障
- 供暖问题
- 环境卫生
- 其他

**处理状态：**
- 0: 待处理
- 1: 处理中
- 2: 已完成
- 3: 已驳回

### 2. 事项投诉
用户可以编辑投诉事项类型和事项描述，提交至社区管理人员。

**支持的投诉类型：**
- 噪音扰民
- 违规停车
- 垃圾处理
- 物业服务
- 安全问题
- 设施损坏
- 其他

**处理状态：**
- 0: 待处理
- 1: 处理中
- 2: 已完成
- 3: 已驳回

## API 接口

### 报事维修接口

#### 1. 提交报事维修

**接口地址：** `POST /api/repair/submit`

**请求头：**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求参数：**
```json
{
  "reportType": "水电维修",
  "description": "厨房水龙头漏水，需要维修"
}
```

**参数说明：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| reportType | String | 是 | 事项类型 |
| description | String | 是 | 事项描述 |

**响应示例：**
```json
{
  "code": 200,
  "message": "提交成功",
  "data": {
    "reportId": 1,
    "userId": 1,
    "reportType": "水电维修",
    "description": "厨房水龙头漏水，需要维修",
    "status": 0,
    "statusText": "待处理",
    "createTime": "2026-01-04 10:30:00",
    "handleTime": null,
    "handleUserId": null,
    "handleResult": null
  }
}
```

#### 2. 查询我的报事维修列表

**接口地址：** `GET /api/repair/my-list`

**请求头：**
```
Authorization: Bearer {token}
```

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | Integer | 否 | 处理状态 0-待处理 1-处理中 2-已完成 3-已驳回 |
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页大小，默认10 |

**请求示例：**
```
GET /api/repair/my-list?status=0&pageNum=1&pageSize=10
```

**响应示例：**
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "records": [
      {
        "reportId": 1,
        "userId": 1,
        "reportType": "水电维修",
        "description": "厨房水龙头漏水，需要维修",
        "status": 0,
        "statusText": "待处理",
        "createTime": "2026-01-04 09:00:00",
        "handleTime": null,
        "handleUserId": null,
        "handleResult": null
      }
    ],
    "total": 10,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

#### 3. 查询报事维修详情

**接口地址：** `GET /api/repair/{reportId}`

**请求头：**
```
Authorization: Bearer {token}
```

**路径参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| reportId | Long | 是 | 报事ID |

**请求示例：**
```
GET /api/repair/1
```

**响应示例：**
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "reportId": 1,
    "userId": 1,
    "reportType": "水电维修",
    "description": "厨房水龙头漏水，需要维修",
    "status": 2,
    "statusText": "已完成",
    "createTime": "2026-01-02 10:15:00",
    "handleTime": "2026-01-02 16:30:00",
    "handleUserId": 1,
    "handleResult": "已安排维修人员上门处理，问题已解决"
  }
}
```

### 事项投诉接口

#### 1. 提交事项投诉

**接口地址：** `POST /api/complaint/submit`

**请求头：**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求参数：**
```json
{
  "complaintType": "噪音扰民",
  "description": "楼上住户深夜装修，噪音严重影响休息"
}
```

**参数说明：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| complaintType | String | 是 | 投诉类型 |
| description | String | 是 | 投诉描述 |

**响应示例：**
```json
{
  "code": 200,
  "message": "提交成功",
  "data": {
    "complaintId": 1,
    "userId": 1,
    "complaintType": "噪音扰民",
    "description": "楼上住户深夜装修，噪音严重影响休息",
    "status": 0,
    "statusText": "待处理",
    "createTime": "2026-01-04 10:30:00",
    "handleTime": null,
    "handleUserId": null,
    "handleResult": null
  }
}
```

#### 2. 查询我的投诉列表

**接口地址：** `GET /api/complaint/my-list`

**请求头：**
```
Authorization: Bearer {token}
```

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | Integer | 否 | 处理状态 0-待处理 1-处理中 2-已完成 3-已驳回 |
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页大小，默认10 |

**请求示例：**
```
GET /api/complaint/my-list?status=0&pageNum=1&pageSize=10
```

**响应示例：**
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "records": [
      {
        "complaintId": 1,
        "userId": 1,
        "complaintType": "噪音扰民",
        "description": "楼上住户深夜装修，噪音严重影响休息",
        "status": 0,
        "statusText": "待处理",
        "createTime": "2026-01-04 09:30:00",
        "handleTime": null,
        "handleUserId": null,
        "handleResult": null
      }
    ],
    "total": 10,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

#### 3. 查询投诉详情

**接口地址：** `GET /api/complaint/{complaintId}`

**请求头：**
```
Authorization: Bearer {token}
```

**路径参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| complaintId | Long | 是 | 投诉ID |

**请求示例：**
```
GET /api/complaint/1
```

**响应示例：**
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "complaintId": 1,
    "userId": 1,
    "complaintType": "噪音扰民",
    "description": "楼上住户深夜装修，噪音严重影响休息",
    "status": 2,
    "statusText": "已完成",
    "createTime": "2026-01-02 11:00:00",
    "handleTime": "2026-01-02 14:00:00",
    "handleUserId": 1,
    "handleResult": "已加强噪音管理，并与楼上住户沟通，问题已解决"
  }
}
```

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权（token无效或过期） |
| 500 | 服务器内部错误 |

## 测试步骤

### 1. 准备工作
1. 确保已经注册并登录，获取token
2. 在Knife4j文档页面（http://localhost:8080/doc.html）点击右上角"文档管理" -> "全局参数设置"
3. 添加参数：
   - 参数名称：`Authorization`
   - 参数值：`Bearer {你的token}`
   - 参数位置：`header`
4. 点击"保存"

### 2. 测试报事维修

#### 2.1 提交报事维修
1. 在Knife4j文档中找到"报事维修"分组
2. 点击"提交报事维修"接口
3. 点击"调试"
4. 填写请求参数：
   ```json
   {
     "reportType": "水电维修",
     "description": "厨房水龙头漏水，需要维修"
   }
   ```
5. 点击"发送"查看响应结果

#### 2.2 查询我的报事维修列表
1. 点击"查询我的报事维修列表"接口
2. 点击"调试"
3. 可选择填写状态参数（0-待处理、1-处理中、2-已完成、3-已驳回）
4. 设置分页参数（pageNum、pageSize）
5. 点击"发送"查看列表结果

#### 2.3 查询报事维修详情
1. 点击"查询报事维修详情"接口
2. 点击"调试"
3. 填写reportId（从列表中获取）
4. 点击"发送"查看详细信息

### 3. 测试事项投诉

#### 3.1 提交事项投诉
1. 在Knife4j文档中找到"事项投诉"分组
2. 点击"提交事项投诉"接口
3. 点击"调试"
4. 填写请求参数：
   ```json
   {
     "complaintType": "噪音扰民",
     "description": "楼上住户深夜装修，噪音严重影响休息"
   }
   ```
5. 点击"发送"查看响应结果

#### 3.2 查询我的投诉列表
1. 点击"查询我的投诉列表"接口
2. 点击"调试"
3. 可选择填写状态参数（0-待处理、1-处理中、2-已完成、3-已驳回）
4. 设置分页参数（pageNum、pageSize）
5. 点击"发送"查看列表结果

#### 3.3 查询投诉详情
1. 点击"查询投诉详情"接口
2. 点击"调试"
3. 填写complaintId（从列表中获取）
4. 点击"发送"查看详细信息

## 示例数据
项目提供了示例数据SQL脚本：`src/main/resources/db/sample_repair_complaint_data.sql`

可以在数据库中执行该脚本来插入测试数据。

## 注意事项
1. 所有接口都需要在请求头中携带有效的token
2. 事项类型和投诉类型建议使用预定义的常量值
3. 描述字段不能为空，建议详细描述问题
4. 提交后的状态默认为"待处理"（status=0）
5. 查询列表接口支持按状态筛选，不传status参数则查询所有状态
6. 查询详情接口会验证记录是否属于当前用户
7. 列表按创建时间倒序排列，最新的记录在前面

