-- 为 sys_user 表添加 age 字段
ALTER TABLE sys_user ADD COLUMN age INT COMMENT '年龄' AFTER gender;

