package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sc.smartcommunitybackend.domain.ParkingSpace;
import org.sc.smartcommunitybackend.domain.SysUser;
import org.sc.smartcommunitybackend.dto.request.ParkingSpaceRequest;
import org.sc.smartcommunitybackend.dto.response.ParkingSpaceResponse;
import org.sc.smartcommunitybackend.exception.BusinessException;
import org.sc.smartcommunitybackend.service.ParkingSpaceService;
import org.sc.smartcommunitybackend.mapper.ParkingSpaceMapper;
import org.sc.smartcommunitybackend.service.SysUserService;
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
* @description 针对表【parking_space(车位信息表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class ParkingSpaceServiceImpl extends ServiceImpl<ParkingSpaceMapper, ParkingSpace>
    implements ParkingSpaceService{

    private static final Logger logger = LoggerFactory.getLogger(ParkingSpaceServiceImpl.class);

    @Autowired
    private SysUserService sysUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ParkingSpaceResponse registerParkingSpace(Long userId, ParkingSpaceRequest request) {
        // 1. 检查车位编号是否已存在
        LambdaQueryWrapper<ParkingSpace> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ParkingSpace::getSpaceNo, request.getSpaceNo());
        ParkingSpace existingSpace = getOne(queryWrapper);
        if (existingSpace != null) {
            throw new BusinessException("车位编号已存在");
        }

        // 2. 检查车牌号是否已绑定（只检查已通过审核的）
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ParkingSpace::getCarNumber, request.getCarNumber())
                   .eq(ParkingSpace::getStatus, org.sc.smartcommunitybackend.constant.ParkingSpaceConstant.STATUS_APPROVED);
        existingSpace = getOne(queryWrapper);
        if (existingSpace != null) {
            throw new BusinessException("该车牌号已绑定其他车位");
        }

        // 3. 创建车位申请（待审核状态）
        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setUserId(userId);
        parkingSpace.setSpaceNo(request.getSpaceNo());
        parkingSpace.setCarNumber(request.getCarNumber());
        parkingSpace.setStatus(org.sc.smartcommunitybackend.constant.ParkingSpaceConstant.STATUS_PENDING);
        parkingSpace.setCreateTime(new Date());
        parkingSpace.setUpdateTime(new Date());

        boolean saved = save(parkingSpace);
        if (!saved) {
            throw new BusinessException("提交车位登记申请失败");
        }

        logger.info("用户{}提交车位登记申请, spaceId: {}, spaceNo: {}", userId, parkingSpace.getSpaceId(), parkingSpace.getSpaceNo());

        // 4. 返回响应
        return convertToResponse(parkingSpace);
    }

    @Override
    public List<ParkingSpaceResponse> getMyParkingSpaces(Long userId) {
        // 1. 查询用户的车位列表
        LambdaQueryWrapper<ParkingSpace> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ParkingSpace::getUserId, userId)
                   .orderByDesc(ParkingSpace::getCreateTime);

        List<ParkingSpace> parkingSpaces = list(queryWrapper);

        // 2. 转换为响应对象
        return parkingSpaces.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<ParkingSpaceResponse> getAllParkingSpaces(
            String spaceNo, String carNumber, Integer pageNum, Integer pageSize) {
        // 1. 构建查询条件
        LambdaQueryWrapper<ParkingSpace> queryWrapper = new LambdaQueryWrapper<>();
        
        // 车位编号模糊查询
        if (spaceNo != null && !spaceNo.trim().isEmpty()) {
            queryWrapper.like(ParkingSpace::getSpaceNo, spaceNo);
        }
        
        // 车牌号模糊查询
        if (carNumber != null && !carNumber.trim().isEmpty()) {
            queryWrapper.like(ParkingSpace::getCarNumber, carNumber);
        }
        
        // 按创建时间倒序排列
        queryWrapper.orderByDesc(ParkingSpace::getCreateTime);
        
        // 2. 分页查询
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<ParkingSpace> page = 
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize);
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<ParkingSpace> resultPage = page(page, queryWrapper);
        
        // 3. 转换为响应对象
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<ParkingSpaceResponse> responsePage = 
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
        org.springframework.beans.BeanUtils.copyProperties(resultPage, responsePage, "records");
        
        responsePage.setRecords(
            resultPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList())
        );
        
        return responsePage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ParkingSpaceResponse updateParkingSpace(Long spaceId, Long userId, ParkingSpaceRequest request) {
        // 1. 查询车位
        ParkingSpace parkingSpace = getById(spaceId);
        if (parkingSpace == null) {
            throw new BusinessException("车位不存在");
        }

        // 2. 验证权限
        if (!parkingSpace.getUserId().equals(userId)) {
            throw new BusinessException("无权修改该车位");
        }

        // 3. 检查车牌号是否已被其他车位绑定
        LambdaQueryWrapper<ParkingSpace> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ParkingSpace::getCarNumber, request.getCarNumber())
                   .ne(ParkingSpace::getSpaceId, spaceId);
        ParkingSpace existingSpace = getOne(queryWrapper);
        if (existingSpace != null) {
            throw new BusinessException("该车牌号已绑定其他车位");
        }

        // 4. 更新车位信息
        parkingSpace.setCarNumber(request.getCarNumber());
        parkingSpace.setUpdateTime(new Date());

        boolean updated = updateById(parkingSpace);
        if (!updated) {
            throw new BusinessException("更新车位失败");
        }

        logger.info("用户{}更新车位成功, spaceId: {}", userId, spaceId);

        // 5. 返回响应
        return convertToResponse(parkingSpace);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteParkingSpace(Long spaceId, Long userId) {
        // 1. 查询车位
        ParkingSpace parkingSpace = getById(spaceId);
        if (parkingSpace == null) {
            throw new BusinessException("车位不存在");
        }

        // 2. 验证权限
        if (!parkingSpace.getUserId().equals(userId)) {
            throw new BusinessException("无权删除该车位");
        }

        // 3. 删除车位
        boolean removed = removeById(spaceId);
        if (removed) {
            logger.info("用户{}删除车位成功, spaceId: {}", userId, spaceId);
        }

        return removed;
    }

    @Override
    public ParkingSpaceResponse getParkingSpaceDetail(Long spaceId, Long userId) {
        // 1. 查询车位
        ParkingSpace parkingSpace = getById(spaceId);
        if (parkingSpace == null) {
            throw new BusinessException("车位不存在");
        }

        // 2. 验证权限
        if (!parkingSpace.getUserId().equals(userId)) {
            throw new BusinessException("无权查看该车位");
        }

        // 3. 返回详情
        return convertToResponse(parkingSpace);
    }

    /**
     * 转换为响应对象
     */
    private ParkingSpaceResponse convertToResponse(ParkingSpace parkingSpace) {
        // 查询用户信息
        SysUser user = sysUserService.getById(parkingSpace.getUserId());
        String userName = user != null ? user.getUserName() : "未知";

        return ParkingSpaceResponse.builder()
                .spaceId(parkingSpace.getSpaceId())
                .userId(parkingSpace.getUserId())
                .userName(userName)
                .spaceNo(parkingSpace.getSpaceNo())
                .carNumber(parkingSpace.getCarNumber())
                .status(parkingSpace.getStatus())
                .statusText(getStatusText(parkingSpace.getStatus()))
                .createTime(parkingSpace.getCreateTime())
                .updateTime(parkingSpace.getUpdateTime())
                .auditTime(parkingSpace.getAuditTime())
                .auditUserId(parkingSpace.getAuditUserId())
                .rejectReason(parkingSpace.getRejectReason())
                .build();
    }

    /**
     * 获取状态描述
     */
    private String getStatusText(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case org.sc.smartcommunitybackend.constant.ParkingSpaceConstant.STATUS_PENDING:
                return "待审核";
            case org.sc.smartcommunitybackend.constant.ParkingSpaceConstant.STATUS_APPROVED:
                return "已通过";
            case org.sc.smartcommunitybackend.constant.ParkingSpaceConstant.STATUS_REJECTED:
                return "已拒绝";
            default:
                return "未知";
        }
    }
}




