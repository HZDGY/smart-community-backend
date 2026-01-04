-- 修改 visitor_register 表结构
-- 添加访客姓名、访客电话和拒绝原因字段

USE smart_community;

-- 添加访客姓名字段
ALTER TABLE `visitor_register` 
ADD COLUMN `visitor_name` VARCHAR(50) NULL COMMENT '访客姓名' AFTER `user_id`;

-- 添加访客电话字段
ALTER TABLE `visitor_register` 
ADD COLUMN `visitor_phone` VARCHAR(20) NULL COMMENT '访客电话' AFTER `visitor_name`;

-- 添加拒绝原因字段
ALTER TABLE `visitor_register` 
ADD COLUMN `reject_reason` VARCHAR(200) NULL COMMENT '拒绝原因' AFTER `audit_user_id`;

-- 验证表结构
DESC visitor_register;

