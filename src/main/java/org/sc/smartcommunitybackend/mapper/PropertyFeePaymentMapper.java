package org.sc.smartcommunitybackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.PropertyFeePayment;

/**
 * 物业费缴纳记录 Mapper 接口
 */
@Mapper
public interface PropertyFeePaymentMapper extends BaseMapper<PropertyFeePayment> {
}
