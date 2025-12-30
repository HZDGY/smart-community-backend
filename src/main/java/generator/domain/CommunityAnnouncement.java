package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 社区公告表
 * @TableName community_announcement
 */
@TableName(value ="community_announcement")
@Data
public class CommunityAnnouncement {
    /**
     * 公告ID
     */
    @TableId(type = IdType.AUTO)
    private Long announce_id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 发布人ID
     */
    private Long publish_user_id;

    /**
     * 发布时间
     */
    private Date publish_time;

    /**
     * 阅读次数
     */
    private Integer read_count;
}