package org.sc.smartcommunitybackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.sc.smartcommunitybackend.domain.ParkingSpace;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sc.smartcommunitybackend.dto.request.ParkingSpaceRequest;
import org.sc.smartcommunitybackend.dto.response.ParkingSpaceResponse;

import java.util.List;

/**
* @author 吴展德
* @description 针对表【parking_space(车位信息表)】的数据库操作Service
* @createDate 2025-12-30 10:46:05
*/
public interface ParkingSpaceService extends IService<ParkingSpace> {

    /**
     * 登记车位
     *
     * @param userId 用户ID
     * @param request 车位请求
     * @return 车位响应
     */
    ParkingSpaceResponse registerParkingSpace(Long userId, ParkingSpaceRequest request);

    /**
     * 查询我的车位列表
     *
     * @param userId 用户ID
     * @return 车位列表
     */
    List<ParkingSpaceResponse> getMyParkingSpaces(Long userId);

    /**
     * 查询所有车位列表（分页）
     *
     * @param spaceNo 车位编号（可选，模糊查询）
     * @param carNumber 车牌号（可选，模糊查询）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    Page<ParkingSpaceResponse> getAllParkingSpaces(String spaceNo, String carNumber, Integer pageNum, Integer pageSize);

    /**
     * 更新车位信息(绑定车牌号)
     *
     * @param spaceId 车位ID
     * @param userId 用户ID
     * @param request 车位请求
     * @return 车位响应
     */
    ParkingSpaceResponse updateParkingSpace(Long spaceId, Long userId, ParkingSpaceRequest request);

    /**
     * 删除车位
     *
     * @param spaceId 车位ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deleteParkingSpace(Long spaceId, Long userId);

    /**
     * 获取车位详情
     *
     * @param spaceId 车位ID
     * @param userId 用户ID
     * @return 车位详情
     */
    ParkingSpaceResponse getParkingSpaceDetail(Long spaceId, Long userId);
}
