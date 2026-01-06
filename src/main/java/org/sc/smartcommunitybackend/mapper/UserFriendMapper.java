package org.sc.smartcommunitybackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.UserFriend;

/**
 * 用户好友Mapper
 */
@Mapper
public interface UserFriendMapper extends BaseMapper<UserFriend> {
}
