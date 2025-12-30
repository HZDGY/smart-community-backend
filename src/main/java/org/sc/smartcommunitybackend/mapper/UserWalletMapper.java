package org.sc.smartcommunitybackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.UserWallet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 吴展德
* @description 针对表【user_wallet(用户钱包表)】的数据库操作Mapper
* @createDate 2025-12-30 10:46:05
* @Entity generator.domain.UserWallet
*/
@Mapper
public interface UserWalletMapper extends BaseMapper<UserWallet> {

}




