package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 服务区域表
 * @TableName service_area
 */
@TableName(value ="service_area")
@Data
public class ServiceArea {
    /**
     * 区域ID
     */
    @TableId(value = "area_id", type = IdType.AUTO)
    private Long area_id;

    /**
     * 区域名称（社区/街道）
     */
    private String area_name;

    /**
     * 父区域ID
     */
    private Long parent_id;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;
}