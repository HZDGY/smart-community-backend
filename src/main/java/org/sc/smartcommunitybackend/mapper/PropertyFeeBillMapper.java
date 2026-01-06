package org.sc.smartcommunitybackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.PropertyFeeBill;

/**
 * 物业费账单 Mapper 接口
 */
@Mapper
public interface PropertyFeeBillMapper extends BaseMapper<PropertyFeeBill> {
}
