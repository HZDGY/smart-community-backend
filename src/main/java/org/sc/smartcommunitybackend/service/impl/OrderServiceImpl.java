package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sc.smartcommunitybackend.domain.Order;
import org.sc.smartcommunitybackend.service.OrderService;
import org.sc.smartcommunitybackend.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴展德
* @description 针对表【order(订单表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

}




