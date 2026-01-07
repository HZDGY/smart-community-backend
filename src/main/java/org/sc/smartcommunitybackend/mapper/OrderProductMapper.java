package org.sc.smartcommunitybackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.OrderProduct;

import java.util.List;

/**
 * 订单商品关联Mapper
 */
@Mapper
public interface OrderProductMapper extends BaseMapper<OrderProduct> {
    
    /**
     * 根据订单ID查询商品列表
     */
    List<OrderProduct> selectByOrderId(Long orderId);
}

