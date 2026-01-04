package org.sc.smartcommunitybackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.sc.smartcommunitybackend.domain.VisitorRegister;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sc.smartcommunitybackend.dto.request.VisitorRegisterRequest;
import org.sc.smartcommunitybackend.dto.response.VisitorRegisterResponse;

/**
* @author 吴展德
* @description 针对表【visitor_register(访客登记表)】的数据库操作Service
* @createDate 2025-12-30 10:46:05
*/
public interface VisitorRegisterService extends IService<VisitorRegister> {

    /**
     * 创建访客登记
     *
     * @param userId 用户ID
     * @param request 登记请求
     * @return 登记响应
     */
    VisitorRegisterResponse createRegister(Long userId, VisitorRegisterRequest request);

    /**
     * 查询我的访客登记列表
     *
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<VisitorRegisterResponse> getMyRegisterList(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 获取访客登记详情
     *
     * @param registerId 登记ID
     * @param userId 用户ID
     * @return 登记详情
     */
    VisitorRegisterResponse getRegisterDetail(Long registerId, Long userId);

    /**
     * 取消访客登记
     *
     * @param registerId 登记ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean cancelRegister(Long registerId, Long userId);
}
