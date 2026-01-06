package org.sc.smartcommunitybackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.PaymentOrder;

/**
 * 支付订单Mapper
 */
@Mapper
public interface PaymentOrderMapper extends BaseMapper<PaymentOrder> {
}
