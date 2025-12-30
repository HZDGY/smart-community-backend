package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sc.smartcommunitybackend.domain.OrderItem;
import org.sc.smartcommunitybackend.service.OrderItemService;
import org.sc.smartcommunitybackend.mapper.OrderItemMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴展德
* @description 针对表【order_item(订单项表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem>
    implements OrderItemService{

}




