package org.sc.smartcommunitybackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sc.smartcommunitybackend.domain.UserWallet;

/**
* @author 吴展德
* @description 针对表【user_wallet(用户钱包表)】的数据库操作Mapper
* @createDate 2025-12-30 10:46:05
* @Entity generator.domain.UserWallet
*/
@Mapper
public interface UserWalletMapper extends BaseMapper<UserWallet> {
    
    /**
     * 根据用户ID查询钱包（加行锁）
     * 用于转账等需要并发控制的场景
     */
    @Select("SELECT * FROM user_wallet WHERE user_id = #{userId} FOR UPDATE")
    UserWallet selectByUserIdForUpdate(@Param("userId") Long userId);
}





