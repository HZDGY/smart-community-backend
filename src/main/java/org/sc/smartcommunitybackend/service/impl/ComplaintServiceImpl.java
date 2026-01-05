package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sc.smartcommunitybackend.constant.ComplaintConstant;
import org.sc.smartcommunitybackend.domain.Complaint;
import org.sc.smartcommunitybackend.dto.request.ComplaintRequest;
import org.sc.smartcommunitybackend.dto.response.ComplaintResponse;
import org.sc.smartcommunitybackend.service.ComplaintService;
import org.sc.smartcommunitybackend.mapper.ComplaintMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author 吴展德
* @description 针对表【complaint(投诉表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class ComplaintServiceImpl extends ServiceImpl<ComplaintMapper, Complaint>
    implements ComplaintService{

    @Override
    public ComplaintResponse submitComplaint(Long userId, ComplaintRequest request) {
        // 创建投诉记录
        Complaint complaint = new Complaint();
        complaint.setUserId(userId);
        complaint.setComplaintType(request.getComplaintType());
        complaint.setDescription(request.getDescription());
        complaint.setLocation(request.getLocation());
        complaint.setStatus(ComplaintConstant.STATUS_PENDING);
        complaint.setCreateTime(new Date());
        
        // 保存到数据库
        this.save(complaint);
        
        // 构造响应
        ComplaintResponse response = new ComplaintResponse();
        BeanUtils.copyProperties(complaint, response);
        response.setStatusText(getStatusText(complaint.getStatus()));
        
        return response;
    }

    @Override
    public Page<ComplaintResponse> getUserComplaintList(Long userId, Integer status, Integer pageNum, Integer pageSize) {
        // 构建查询条件
        LambdaQueryWrapper<Complaint> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Complaint::getUserId, userId);
        
        // 如果指定了状态，添加状态过滤
        if (status != null) {
            queryWrapper.eq(Complaint::getStatus, status);
        }
        
        // 按创建时间倒序排列
        queryWrapper.orderByDesc(Complaint::getCreateTime);
        
        // 分页查询
        Page<Complaint> page = new Page<>(pageNum, pageSize);
        Page<Complaint> resultPage = this.page(page, queryWrapper);
        
        // 转换为响应对象
        Page<ComplaintResponse> responsePage = new Page<>();
        BeanUtils.copyProperties(resultPage, responsePage, "records");
        
        responsePage.setRecords(
            resultPage.getRecords().stream()
                .map(this::convertToResponse)
                .toList()
        );
        
        return responsePage;
    }

    @Override
    public ComplaintResponse getComplaintDetail(Long userId, Long complaintId) {
        // 查询投诉记录
        LambdaQueryWrapper<Complaint> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Complaint::getComplaintId, complaintId)
                   .eq(Complaint::getUserId, userId);
        
        Complaint complaint = this.getOne(queryWrapper);
        
        if (complaint == null) {
            throw new RuntimeException("投诉记录不存在");
        }
        
        return convertToResponse(complaint);
    }
    
    /**
     * 转换为响应对象
     */
    private ComplaintResponse convertToResponse(Complaint complaint) {
        ComplaintResponse response = new ComplaintResponse();
        BeanUtils.copyProperties(complaint, response);
        response.setStatusText(getStatusText(complaint.getStatus()));
        return response;
    }
    
    /**
     * 获取状态描述
     */
    private String getStatusText(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case ComplaintConstant.STATUS_PENDING:
                return "待处理";
            case ComplaintConstant.STATUS_PROCESSING:
                return "处理中";
            case ComplaintConstant.STATUS_COMPLETED:
                return "已完成";
            case ComplaintConstant.STATUS_REJECTED:
                return "已驳回";
            default:
                return "未知";
        }
    }
}




