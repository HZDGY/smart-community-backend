package org.sc.smartcommunitybackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.MockPaymentRecord;

/**
 * 模拟支付记录Mapper
 */
@Mapper
public interface MockPaymentRecordMapper extends BaseMapper<MockPaymentRecord> {
}
