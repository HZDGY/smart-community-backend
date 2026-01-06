-- ============================================
-- 智慧社区论坛系统数据库初始化脚本
-- ============================================

-- 1. 论坛板块表
CREATE TABLE IF NOT EXISTS forum_section (
    section_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '板块ID',
    section_name VARCHAR(100) NOT NULL COMMENT '板块名称',
    section_desc VARCHAR(500) COMMENT '板块描述',
    icon_url VARCHAR(255) COMMENT '板块图标URL',
    sort_order INT DEFAULT 0 COMMENT '排序序号',
    post_count INT DEFAULT 0 COMMENT '帖子数量',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论坛板块表';

-- 2. 帖子表
CREATE TABLE IF NOT EXISTS forum_post (
    post_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '帖子ID',
    section_id BIGINT NOT NULL COMMENT '板块ID',
    user_id BIGINT NOT NULL COMMENT '发帖用户ID',
    title VARCHAR(200) NOT NULL COMMENT '帖子标题',
    content TEXT NOT NULL COMMENT '帖子内容',
    images VARCHAR(1000) COMMENT '图片URL列表，逗号分隔',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    comment_count INT DEFAULT 0 COMMENT '评论数',
    collect_count INT DEFAULT 0 COMMENT '收藏数',
    is_top TINYINT DEFAULT 0 COMMENT '是否置顶 0-否 1-是',
    is_essence TINYINT DEFAULT 0 COMMENT '是否精华 0-否 1-是',
    status TINYINT DEFAULT 1 COMMENT '状态 0-已删除 1-正常 2-待审核',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_section_id (section_id),
    INDEX idx_user_id (user_id),
    INDEX idx_create_time (create_time),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论坛帖子表';

-- 3. 评论表
CREATE TABLE IF NOT EXISTS forum_comment (
    comment_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    user_id BIGINT NOT NULL COMMENT '评论用户ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父评论ID，0表示一级评论',
    reply_to_user_id BIGINT COMMENT '回复的用户ID',
    content TEXT NOT NULL COMMENT '评论内容',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    status TINYINT DEFAULT 1 COMMENT '状态 0-已删除 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_post_id (post_id),
    INDEX idx_user_id (user_id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论坛评论表';

-- 4. 帖子点赞表
CREATE TABLE IF NOT EXISTS forum_post_like (
    like_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '点赞ID',
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_post_user (post_id, user_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子点赞表';

-- 5. 帖子收藏表
CREATE TABLE IF NOT EXISTS forum_post_collect (
    collect_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_post_user (post_id, user_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子收藏表';

-- 6. 评论点赞表
CREATE TABLE IF NOT EXISTS forum_comment_like (
    like_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '点赞ID',
    comment_id BIGINT NOT NULL COMMENT '评论ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_comment_user (comment_id, user_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论点赞表';

-- 7. 好友关系表
CREATE TABLE IF NOT EXISTS user_friend (
    friend_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '好友关系ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    friend_user_id BIGINT NOT NULL COMMENT '好友用户ID',
    remark VARCHAR(50) COMMENT '好友备注',
    status TINYINT DEFAULT 0 COMMENT '状态 0-待确认 1-已同意 2-已拒绝',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_friend_user_id (friend_user_id),
    INDEX idx_status (status),
    UNIQUE KEY uk_user_friend (user_id, friend_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户好友表';

-- 8. 私信表
CREATE TABLE IF NOT EXISTS private_message (
    message_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '消息ID',
    from_user_id BIGINT NOT NULL COMMENT '发送者ID',
    to_user_id BIGINT NOT NULL COMMENT '接收者ID',
    content TEXT NOT NULL COMMENT '消息内容',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读 0-未读 1-已读',
    status TINYINT DEFAULT 1 COMMENT '状态 0-已删除 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    read_time DATETIME COMMENT '阅读时间',
    INDEX idx_from_user (from_user_id),
    INDEX idx_to_user (to_user_id),
    INDEX idx_create_time (create_time),
    INDEX idx_is_read (is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='私信表';

-- 9. 用户通知表
CREATE TABLE IF NOT EXISTS user_notification (
    notification_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '通知ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    type TINYINT NOT NULL COMMENT '通知类型 1-系统通知 2-点赞通知 3-评论通知 4-好友申请 5-私信通知',
    title VARCHAR(100) NOT NULL COMMENT '通知标题',
    content VARCHAR(500) COMMENT '通知内容',
    related_id BIGINT COMMENT '关联ID（如帖子ID、评论ID等）',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读 0-未读 1-已读',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_create_time (create_time),
    INDEX idx_is_read (is_read),
    INDEX idx_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户通知表';

-- ============================================
-- 初始化板块数据
-- ============================================

INSERT INTO forum_section (section_name, section_desc, icon_url, sort_order, status) VALUES
('社区公告', '社区官方公告和重要通知', NULL, 1, 1),
('邻里互助', '邻里之间互帮互助，共建和谐社区', NULL, 2, 1),
('生活分享', '分享生活中的点点滴滴', NULL, 3, 1),
('二手交易', '社区内二手物品交易', NULL, 4, 1),
('宠物天地', '宠物爱好者的交流天地', NULL, 5, 1),
('美食推荐', '分享美食，推荐餐厅', NULL, 6, 1),
('运动健身', '运动健身经验分享', NULL, 7, 1),
('育儿交流', '育儿经验交流分享', NULL, 8, 1),
('投诉建议', '对社区管理的投诉和建议', NULL, 9, 1),
('闲聊灌水', '随意聊天，轻松灌水', NULL, 10, 1);

-- ============================================
-- 测试数据（可选）
-- ============================================

-- 插入测试帖子（假设user_id=1存在）
-- INSERT INTO forum_post (section_id, user_id, title, content, view_count, like_count, comment_count) VALUES
-- (2, 1, '寻找丢失的钥匙', '今天下午在小区花园丢失了一串钥匙，上面有小熊挂件，如有拾到请联系我，谢谢！', 50, 5, 3),
-- (3, 1, '今天的晚霞真美', '傍晚在阳台看到超美的晚霞，分享给大家！', 120, 15, 8),
-- (5, 1, '新来的小猫咪', '家里新来了一只小橘猫，超级可爱，大家来看看！', 200, 30, 12);

COMMIT;
