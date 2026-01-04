-- 为车位信息表添加审核相关字段

ALTER TABLE parking_space
ADD COLUMN status TINYINT DEFAULT 0 COMMENT '审核状态 0-待审核 1-已通过 2-已拒绝' AFTER car_number,
ADD COLUMN audit_time DATETIME NULL COMMENT '审核时间' AFTER update_time,
ADD COLUMN audit_user_id BIGINT NULL COMMENT '审核人ID' AFTER audit_time,
ADD COLUMN reject_reason VARCHAR(200) NULL COMMENT '拒绝原因' AFTER audit_user_id,
ADD INDEX idx_status (status),
ADD CONSTRAINT fk_parking_audit_user FOREIGN KEY (audit_user_id) REFERENCES sys_user(user_id) ON DELETE SET NULL;

-- 更新现有数据的状态为已通过
UPDATE parking_space SET status = 1 WHERE status IS NULL OR status = 0;

