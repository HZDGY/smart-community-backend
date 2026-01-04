package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sc.smartcommunitybackend.constant.VisitorConstant;
import org.sc.smartcommunitybackend.domain.SysUser;
import org.sc.smartcommunitybackend.domain.VisitorRegister;
import org.sc.smartcommunitybackend.dto.request.VisitorRegisterRequest;
import org.sc.smartcommunitybackend.dto.response.VisitorRegisterResponse;
import org.sc.smartcommunitybackend.exception.BusinessException;
import org.sc.smartcommunitybackend.service.SysUserService;
import org.sc.smartcommunitybackend.service.VisitorRegisterService;
import org.sc.smartcommunitybackend.mapper.VisitorRegisterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 吴展德
* @description 针对表【visitor_register(访客登记表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class VisitorRegisterServiceImpl extends ServiceImpl<VisitorRegisterMapper, VisitorRegister>
    implements VisitorRegisterService{

    private static final Logger logger = LoggerFactory.getLogger(VisitorRegisterServiceImpl.class);

    @Autowired
    private SysUserService sysUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitorRegisterResponse createRegister(Long userId, VisitorRegisterRequest request) {
        // 1. 验证时间
        if (request.getAllowTime().after(request.getValidDate())) {
            throw new BusinessException("放行时间不能晚于有效日期");
        }

        // 2. 创建访客登记
        VisitorRegister register = new VisitorRegister();
        register.setUserId(userId);
        register.setVisitorName(request.getVisitorName());
        register.setVisitorPhone(request.getVisitorPhone());
        register.setVisitPurpose(request.getVisitPurpose());
        register.setAllowTime(request.getAllowTime());
        register.setValidDate(request.getValidDate());
        register.setStatus(VisitorConstant.STATUS_PENDING);
        register.setCreateTime(new Date());

        boolean saved = save(register);
        if (!saved) {
            throw new BusinessException("创建访客登记失败");
        }

        logger.info("用户{}创建访客登记成功, registerId: {}", userId, register.getRegisterId());

        // 3. 返回响应
        return convertToResponse(register);
    }

    @Override
    public Page<VisitorRegisterResponse> getMyRegisterList(Long userId, Integer pageNum, Integer pageSize) {
        // 1. 构建查询条件
        LambdaQueryWrapper<VisitorRegister> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VisitorRegister::getUserId, userId)
                   .orderByDesc(VisitorRegister::getCreateTime);

        // 2. 分页查询
        Page<VisitorRegister> page = new Page<>(pageNum, pageSize);
        Page<VisitorRegister> registerPage = page(page, queryWrapper);

        // 3. 转换为响应对象
        List<VisitorRegisterResponse> responseList = registerPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        // 4. 构建分页响应
        Page<VisitorRegisterResponse> responsePage = new Page<>(
                registerPage.getCurrent(),
                registerPage.getSize(),
                registerPage.getTotal()
        );
        responsePage.setRecords(responseList);

        return responsePage;
    }

    @Override
    public VisitorRegisterResponse getRegisterDetail(Long registerId, Long userId) {
        // 1. 查询登记信息
        VisitorRegister register = getById(registerId);
        if (register == null) {
            throw new BusinessException("访客登记不存在");
        }

        // 2. 验证权限
        if (!register.getUserId().equals(userId)) {
            throw new BusinessException("无权查看该访客登记");
        }

        // 3. 返回详情
        return convertToResponse(register);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelRegister(Long registerId, Long userId) {
        // 1. 查询登记信息
        VisitorRegister register = getById(registerId);
        if (register == null) {
            throw new BusinessException("访客登记不存在");
        }

        // 2. 验证权限
        if (!register.getUserId().equals(userId)) {
            throw new BusinessException("无权取消该访客登记");
        }

        // 3. 验证状态
        if (!VisitorConstant.STATUS_PENDING.equals(register.getStatus())) {
            throw new BusinessException("只能取消待审核的访客登记");
        }

        // 4. 删除登记
        boolean removed = removeById(registerId);
        if (removed) {
            logger.info("用户{}取消访客登记成功, registerId: {}", userId, registerId);
        }

        return removed;
    }

    /**
     * 转换为响应对象
     */
    private VisitorRegisterResponse convertToResponse(VisitorRegister register) {
        // 查询用户信息
        SysUser user = sysUserService.getById(register.getUserId());
        String userName = user != null ? user.getUserName() : "未知";

        // 查询审核人信息
        String auditUserName = null;
        if (register.getAuditUserId() != null) {
            SysUser auditUser = sysUserService.getById(register.getAuditUserId());
            auditUserName = auditUser != null ? auditUser.getUserName() : "未知";
        }

        // 状态描述
        String statusDesc = getStatusDesc(register.getStatus());

        return VisitorRegisterResponse.builder()
                .registerId(register.getRegisterId())
                .userId(register.getUserId())
                .userName(userName)
                .visitorName(register.getVisitorName())
                .visitorPhone(register.getVisitorPhone())
                .visitPurpose(register.getVisitPurpose())
                .allowTime(register.getAllowTime())
                .validDate(register.getValidDate())
                .status(register.getStatus())
                .statusDesc(statusDesc)
                .createTime(register.getCreateTime())
                .auditTime(register.getAuditTime())
                .auditUserName(auditUserName)
                .rejectReason(register.getRejectReason())
                .build();
    }

    /**
     * 获取状态描述
     */
    private String getStatusDesc(Integer status) {
        if (VisitorConstant.STATUS_PENDING.equals(status)) {
            return "待审核";
        } else if (VisitorConstant.STATUS_APPROVED.equals(status)) {
            return "已通过";
        } else if (VisitorConstant.STATUS_REJECTED.equals(status)) {
            return "已拒绝";
        }
        return "未知";
    }
}




