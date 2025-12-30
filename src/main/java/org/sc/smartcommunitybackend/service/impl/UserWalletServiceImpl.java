package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sc.smartcommunitybackend.domain.UserWallet;
import org.sc.smartcommunitybackend.service.UserWalletService;
import org.sc.smartcommunitybackend.mapper.UserWalletMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴展德
* @description 针对表【user_wallet(用户钱包表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class UserWalletServiceImpl extends ServiceImpl<UserWalletMapper, UserWallet>
    implements UserWalletService{

}




