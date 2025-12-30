package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 门店表
 * @TableName store
 */
@TableName(value ="store")
@Data
public class Store {
    /**
     * 门店ID
     */
    @TableId(type = IdType.AUTO)
    private Long store_id;

    /**
     * 门店名称
     */
    private String store_name;

    /**
     * 所属区域ID
     */
    private Long area_id;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 营业时间
     */
    private String business_hours;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;
}