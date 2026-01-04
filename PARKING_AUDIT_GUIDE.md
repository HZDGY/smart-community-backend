# 车位登记审核功能说明

## 功能概述
车位登记功能已升级为需要审核的模式。用户提交车位登记申请后，需要等待社区管理人员审核通过才能生效。

## 审核流程

### 1. 用户提交申请
- 用户通过 `POST /api/parking/register` 接口提交车位登记申请
- 填写车位编号和车牌号
- 提交后状态为 **待审核**（status=0）

### 2. 等待审核
- 申请提交后，用户可以通过 `GET /api/parking/list` 查看申请状态
- 状态包括：
  - **0 - 待审核**：申请已提交，等待管理员审核
  - **1 - 已通过**：审核通过，车位登记生效
  - **2 - 已拒绝**：审核未通过，可查看拒绝原因

### 3. 审核结果
- **审核通过**：车位登记生效，可以正常使用
- **审核拒绝**：申请被拒绝，系统会记录拒绝原因，用户可以查看并重新申请

## 数据库变更

### 新增字段
为 `parking_space` 表添加了以下字段：

```sql
ALTER TABLE parking_space
ADD COLUMN status TINYINT DEFAULT 0 COMMENT '审核状态 0-待审核 1-已通过 2-已拒绝',
ADD COLUMN audit_time DATETIME NULL COMMENT '审核时间',
ADD COLUMN audit_user_id BIGINT NULL COMMENT '审核人ID',
ADD COLUMN reject_reason VARCHAR(200) NULL COMMENT '拒绝原因';
```

### 执行迁移
运行以下SQL脚本进行数据库迁移：
```bash
src/main/resources/db/alter_parking_space.sql
```

## API 接口变更

### 1. 提交车位登记申请
**接口：** `POST /api/parking/register`

**变更说明：**
- 接口名称从"登记车位"改为"提交车位登记申请"
- 提交后状态为待审核，不再立即生效
- 返回消息改为"提交车位登记申请成功，请等待审核"

**请求示例：**
```json
{
  "spaceNo": "A-101",
  "carNumber": "京A12345"
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "提交车位登记申请成功，请等待审核",
  "data": {
    "spaceId": 1,
    "userId": 1,
    "userName": "张三",
    "spaceNo": "A-101",
    "carNumber": "京A12345",
    "status": 0,
    "statusText": "待审核",
    "createTime": "2026-01-04 10:00:00",
    "updateTime": "2026-01-04 10:00:00",
    "auditTime": null,
    "auditUserId": null,
    "rejectReason": null
  }
}
```

### 2. 查询我的车位列表
**接口：** `GET /api/parking/list`

**变更说明：**
- 返回数据中增加了审核相关字段
- 可以查看所有状态的申请记录

**响应示例：**
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "spaceId": 1,
      "userId": 1,
      "userName": "张三",
      "spaceNo": "A-101",
      "carNumber": "京A12345",
      "status": 1,
      "statusText": "已通过",
      "createTime": "2026-01-04 10:00:00",
      "updateTime": "2026-01-04 10:00:00",
      "auditTime": "2026-01-04 10:30:00",
      "auditUserId": 1,
      "rejectReason": null
    },
    {
      "spaceId": 2,
      "userId": 1,
      "userName": "张三",
      "spaceNo": "A-102",
      "carNumber": "京B67890",
      "status": 0,
      "statusText": "待审核",
      "createTime": "2026-01-04 11:00:00",
      "updateTime": "2026-01-04 11:00:00",
      "auditTime": null,
      "auditUserId": null,
      "rejectReason": null
    },
    {
      "spaceId": 3,
      "userId": 1,
      "userName": "张三",
      "spaceNo": "C-301",
      "carNumber": "京L99999",
      "status": 2,
      "statusText": "已拒绝",
      "createTime": "2026-01-03 08:00:00",
      "updateTime": "2026-01-03 08:00:00",
      "auditTime": "2026-01-03 08:30:00",
      "auditUserId": 1,
      "rejectReason": "车位编号不存在，请核实后重新申请"
    }
  ]
}
```

### 3. 查询所有车位列表
**接口：** `GET /api/parking/all`

**变更说明：**
- 返回数据中增加了审核相关字段
- 可以按状态筛选查询

## 业务逻辑变更

### 1. 车位编号唯一性检查
- 检查车位编号是否已存在（不区分审核状态）
- 如果车位编号已被使用，无法提交申请

### 2. 车牌号唯一性检查
- 只检查**已通过审核**的车位
- 待审核和已拒绝的车位不影响车牌号的绑定

### 3. 状态说明
| 状态值 | 状态名称 | 说明 |
|--------|---------|------|
| 0 | 待审核 | 申请已提交，等待管理员审核 |
| 1 | 已通过 | 审核通过，车位登记生效 |
| 2 | 已拒绝 | 审核未通过，可查看拒绝原因 |

## 示例数据

示例数据已更新，包含不同审核状态的车位：
- **已通过**：A-101, A-102, A-103, A-104, A-105, A-106, B-201, B-202, C-303, C-304, D-401, D-403, U1-001, U1-002, U1-003, U2-001
- **待审核**：B-203, B-204, D-402
- **已拒绝**：C-301（车位编号不存在），C-302（车牌号格式不正确），D-404（该车位已分配给其他业主）

## 测试步骤

### 1. 提交车位登记申请
1. 登录获取token
2. 调用 `POST /api/parking/register` 提交申请
3. 查看返回的status字段，应为0（待审核）

### 2. 查看申请状态
1. 调用 `GET /api/parking/list` 查看我的车位列表
2. 找到刚才提交的申请，查看status和statusText

### 3. 查看审核结果
- 如果status=1，表示审核通过
- 如果status=2，查看rejectReason了解拒绝原因

## 注意事项

1. **现有数据迁移**：执行迁移脚本后，现有车位数据的状态会自动设置为"已通过"（status=1）
2. **车牌号检查**：只有已通过审核的车位才会进行车牌号唯一性检查
3. **重新申请**：如果申请被拒绝，用户可以查看拒绝原因后重新提交申请
4. **审核权限**：审核功能需要管理员权限（后续实现）

## 相关文件

- **数据库迁移**：`src/main/resources/db/alter_parking_space.sql`
- **示例数据**：`src/main/resources/db/sample_visitor_parking_data.sql`
- **实体类**：`src/main/java/org/sc/smartcommunitybackend/domain/ParkingSpace.java`
- **常量类**：`src/main/java/org/sc/smartcommunitybackend/constant/ParkingSpaceConstant.java`
- **服务实现**：`src/main/java/org/sc/smartcommunitybackend/service/impl/ParkingSpaceServiceImpl.java`
- **控制器**：`src/main/java/org/sc/smartcommunitybackend/controller/ParkingController.java`
- **响应DTO**：`src/main/java/org/sc/smartcommunitybackend/dto/response/ParkingSpaceResponse.java`

