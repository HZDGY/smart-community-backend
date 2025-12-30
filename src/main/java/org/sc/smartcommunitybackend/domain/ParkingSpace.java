package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 车位信息表
 * @TableName parking_space
 */
@TableName(value ="parking_space")
@Data
public class ParkingSpace {
    /**
     * 车位ID
     */
    @TableId(type = IdType.AUTO)
    private Long space_id;

    /**
     * 用户ID
     */
    private Long user_id;

    /**
     * 车位编号
     */
    private String space_no;

    /**
     * 绑定车牌号
     */
    private String car_number;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;
}