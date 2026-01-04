package org.sc.smartcommunitybackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.sc.smartcommunitybackend.domain.Complaint;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sc.smartcommunitybackend.dto.request.ComplaintRequest;
import org.sc.smartcommunitybackend.dto.response.ComplaintResponse;

/**
* @author 吴展德
* @description 针对表【complaint(投诉表)】的数据库操作Service
* @createDate 2025-12-30 10:46:05
*/
public interface ComplaintService extends IService<Complaint> {

    /**
     * 提交事项投诉
     * @param userId 用户ID
     * @param request 投诉请求
     * @return 投诉响应
     */
    ComplaintResponse submitComplaint(Long userId, ComplaintRequest request);

    /**
     * 查询用户的投诉列表
     * @param userId 用户ID
     * @param status 处理状态（可选）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    Page<ComplaintResponse> getUserComplaintList(Long userId, Integer status, Integer pageNum, Integer pageSize);

    /**
     * 查询投诉详情
     * @param userId 用户ID
     * @param complaintId 投诉ID
     * @return 投诉响应
     */
    ComplaintResponse getComplaintDetail(Long userId, Long complaintId);
}
