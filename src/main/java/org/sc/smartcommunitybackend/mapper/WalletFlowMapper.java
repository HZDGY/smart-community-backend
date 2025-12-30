package org.sc.smartcommunitybackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.WalletFlow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 吴展德
* @description 针对表【wallet_flow(钱包流水表)】的数据库操作Mapper
* @createDate 2025-12-30 10:46:05
* @Entity generator.domain.WalletFlow
*/
@Mapper
public interface WalletFlowMapper extends BaseMapper<WalletFlow> {

}




