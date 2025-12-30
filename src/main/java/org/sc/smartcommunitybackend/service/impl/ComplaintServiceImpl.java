package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sc.smartcommunitybackend.domain.Complaint;
import org.sc.smartcommunitybackend.service.ComplaintService;
import org.sc.smartcommunitybackend.mapper.ComplaintMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴展德
* @description 针对表【complaint(投诉表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class ComplaintServiceImpl extends ServiceImpl<ComplaintMapper, Complaint>
    implements ComplaintService{

}




