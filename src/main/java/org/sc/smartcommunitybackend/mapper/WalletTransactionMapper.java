package org.sc.smartcommunitybackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.WalletTransaction;

/**
 * 钱包交易记录 Mapper 接口
 */
@Mapper
public interface WalletTransactionMapper extends BaseMapper<WalletTransaction> {
}
