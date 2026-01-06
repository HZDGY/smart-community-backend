# 智慧社区论坛功能使用指南

## 功能概述

本系统实现了一个完整的类似贴吧的社区交流功能，包括：

1. **论坛板块管理** - 支持多个讨论板块
2. **帖子管理** - 发帖、编辑、删除、点赞、收藏、置顶、精华
3. **评论回复** - 支持多级评论和点赞
4. **好友系统** - 添加好友、好友申请审核、好友列表
5. **私信功能** - 用户间私信交流
6. **通知系统** - 系统通知和消息提醒

## 数据库初始化

### 1. 执行数据库脚本

运行项目根目录下的 `forum_system_init.sql` 文件：

```sql
-- 在MySQL中执行
source /path/to/forum_system_init.sql;
```

或者直接复制SQL内容到MySQL客户端执行。

### 2. 初始化数据

脚本会自动创建以下表：
- `forum_section` - 论坛板块表
- `forum_post` - 帖子表
- `forum_comment` - 评论表
- `forum_post_like` - 帖子点赞表
- `forum_post_collect` - 帖子收藏表
- `forum_comment_like` - 评论点赞表
- `user_friend` - 好友关系表
- `private_message` - 私信表
- `user_notification` - 通知表

并自动插入10个默认板块。

## API接口说明

### 1. 论坛板块接口

#### 获取板块列表
```
GET /forum/section/list
```

**响应示例：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "sectionId": 1,
      "sectionName": "社区公告",
      "sectionDesc": "社区官方公告和重要通知",
      "postCount": 0,
      "status": 1
    }
  ]
}
```

### 2. 帖子管理接口

#### 发布帖子
```
POST /forum/post/create
Authorization: Bearer {token}
```

**请求体：**
```json
{
  "sectionId": 1,
  "title": "帖子标题",
  "content": "帖子内容",
  "images": "http://example.com/1.jpg,http://example.com/2.jpg"
}
```

#### 获取帖子列表
```
POST /forum/post/list
```

**请求体：**
```json
{
  "sectionId": 1,
  "keyword": "搜索关键词",
  "isEssence": 0,
  "sortBy": "latest",
  "pageNum": 1,
  "pageSize": 10
}
```

**sortBy可选值：**
- `latest` - 最新（默认）
- `hot` - 最热
- `essence` - 精华

#### 获取帖子详情
```
GET /forum/post/{postId}
```

#### 更新帖子
```
PUT /forum/post/{postId}
Authorization: Bearer {token}
```

#### 删除帖子
```
DELETE /forum/post/{postId}
Authorization: Bearer {token}
```

#### 点赞/取消点赞帖子
```
POST /forum/post/{postId}/like
DELETE /forum/post/{postId}/like
Authorization: Bearer {token}
```

#### 收藏/取消收藏帖子
```
POST /forum/post/{postId}/collect
DELETE /forum/post/{postId}/collect
Authorization: Bearer {token}
```

#### 我的帖子
```
GET /forum/post/my?pageNum=1&pageSize=10
Authorization: Bearer {token}
```

#### 我收藏的帖子
```
GET /forum/post/my/collected?pageNum=1&pageSize=10
Authorization: Bearer {token}
```

### 3. 评论管理接口

#### 发表评论
```
POST /forum/comment/create
Authorization: Bearer {token}
```

**请求体：**
```json
{
  "postId": 1,
  "parentId": 0,
  "replyToUserId": null,
  "content": "评论内容"
}
```

**说明：**
- `parentId` 为 0 表示一级评论
- `parentId` 不为 0 表示回复某条评论
- `replyToUserId` 表示回复的用户ID（可选）

#### 获取帖子评论
```
GET /forum/comment/post/{postId}?pageNum=1&pageSize=10
```

**响应包含树形结构的评论列表**

#### 删除评论
```
DELETE /forum/comment/{commentId}
Authorization: Bearer {token}
```

#### 点赞/取消点赞评论
```
POST /forum/comment/{commentId}/like
DELETE /forum/comment/{commentId}/like
Authorization: Bearer {token}
```

### 4. 好友管理接口

#### 发送好友申请
```
POST /friend/request
Authorization: Bearer {token}
```

**请求体：**
```json
{
  "friendUserId": 2,
  "remark": "好友备注"
}
```

#### 接受好友申请
```
POST /friend/{friendId}/accept
Authorization: Bearer {token}
```

#### 拒绝好友申请
```
POST /friend/{friendId}/reject
Authorization: Bearer {token}
```

#### 删除好友
```
DELETE /friend/{friendUserId}
Authorization: Bearer {token}
```

#### 获取好友列表
```
GET /friend/list?pageNum=1&pageSize=20
Authorization: Bearer {token}
```

#### 获取好友申请列表
```
GET /friend/requests?pageNum=1&pageSize=20
Authorization: Bearer {token}
```

### 5. 私信管理接口

#### 发送私信
```
POST /message/send
Authorization: Bearer {token}
```

**请求体：**
```json
{
  "toUserId": 2,
  "content": "私信内容"
}
```

#### 获取聊天记录
```
GET /message/chat/{otherUserId}?pageNum=1&pageSize=20
Authorization: Bearer {token}
```

#### 获取会话列表
```
GET /message/conversations?pageNum=1&pageSize=20
Authorization: Bearer {token}
```

#### 标记消息已读
```
POST /message/{messageId}/read
Authorization: Bearer {token}
```

#### 标记所有消息已读
```
POST /message/read-all/{otherUserId}
Authorization: Bearer {token}
```

#### 获取未读消息数量
```
GET /message/unread-count
Authorization: Bearer {token}
```

#### 删除消息
```
DELETE /message/{messageId}
Authorization: Bearer {token}
```

### 6. 通知管理接口

#### 获取通知列表
```
GET /notification/list?pageNum=1&pageSize=20
Authorization: Bearer {token}
```

#### 标记通知已读
```
POST /notification/{notificationId}/read
Authorization: Bearer {token}
```

#### 标记所有通知已读
```
POST /notification/read-all
Authorization: Bearer {token}
```

#### 获取未读通知数量
```
GET /notification/unread-count
Authorization: Bearer {token}
```

#### 删除通知
```
DELETE /notification/{notificationId}
Authorization: Bearer {token}
```

## 使用流程示例

### 1. 发帖流程

1. 用户登录获取token
2. 调用 `GET /forum/section/list` 获取板块列表
3. 选择板块，调用 `POST /forum/post/create` 发布帖子
4. 其他用户可以查看、点赞、收藏、评论该帖子

### 2. 评论流程

1. 用户查看帖子详情
2. 调用 `POST /forum/comment/create` 发表评论
3. 其他用户可以回复评论（设置parentId）
4. 支持多级评论嵌套

### 3. 好友流程

1. 用户A调用 `POST /friend/request` 向用户B发送好友申请
2. 用户B调用 `GET /friend/requests` 查看好友申请
3. 用户B调用 `POST /friend/{friendId}/accept` 接受申请
4. 双方成为好友，可以互相发送私信

### 4. 私信流程

1. 确认双方是好友关系（可选）
2. 调用 `POST /message/send` 发送私信
3. 对方调用 `GET /message/conversations` 查看会话列表
4. 调用 `GET /message/chat/{otherUserId}` 查看聊天记录
5. 调用 `POST /message/{messageId}/read` 标记已读

## 权限说明

### 需要登录的接口
所有带 `Authorization: Bearer {token}` 的接口都需要用户登录。

### 权限控制
- **发帖/评论**：所有登录用户
- **编辑/删除帖子**：仅帖子作者
- **编辑/删除评论**：仅评论作者
- **置顶/精华**：需要管理员权限（待实现）
- **好友申请**：所有登录用户
- **私信**：所有登录用户

## 注意事项

1. **图片上传**：帖子中的图片需要先通过文件上传接口上传，然后将返回的URL填入images字段
2. **分页参数**：默认pageNum=1, pageSize=10
3. **软删除**：帖子和评论采用软删除，status=0表示已删除
4. **点赞去重**：同一用户对同一帖子/评论只能点赞一次
5. **好友关系**：好友关系是双向的，接受申请后会自动创建反向关系

## API文档

启动项目后，访问 Knife4j 文档：
```
http://localhost:8080/doc.html
```

可以在线测试所有API接口。

## 测试建议

1. 使用Postman或Knife4j测试所有接口
2. 创建多个测试用户进行交互测试
3. 测试边界情况（如重复点赞、删除不存在的帖子等）
4. 验证权限控制是否正确
5. 检查数据统计是否准确（点赞数、评论数等）

## 扩展功能建议

1. **内容审核**：添加帖子和评论的审核机制
2. **敏感词过滤**：对帖子和评论内容进行敏感词过滤
3. **用户等级**：根据发帖、评论等行为计算用户等级
4. **勋章系统**：为活跃用户颁发勋章
5. **话题标签**：支持为帖子添加话题标签
6. **搜索优化**：使用Elasticsearch实现全文搜索
7. **实时通知**：使用WebSocket实现实时消息推送
8. **图片压缩**：对上传的图片进行压缩处理
9. **表情包支持**：支持在评论和私信中使用表情包
10. **举报功能**：允许用户举报不良内容
