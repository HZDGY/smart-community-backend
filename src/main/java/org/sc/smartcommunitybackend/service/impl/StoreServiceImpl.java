package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.domain.Store;
import org.sc.smartcommunitybackend.dto.request.StoreListRequest;
import org.sc.smartcommunitybackend.dto.response.StoreVO;
import org.sc.smartcommunitybackend.service.StoreService;
import org.sc.smartcommunitybackend.mapper.StoreMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 吴展德
* @description 针对表【store(门店表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Slf4j
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store>
    implements StoreService{


}




