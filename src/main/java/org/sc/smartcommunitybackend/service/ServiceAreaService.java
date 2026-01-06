package org.sc.smartcommunitybackend.service;

import org.sc.smartcommunitybackend.domain.ServiceArea;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sc.smartcommunitybackend.dto.request.ServiceAreaPageRequest;
import org.sc.smartcommunitybackend.dto.request.ServiceAreaRequest;
import org.sc.smartcommunitybackend.dto.response.PageResult;

/**
* @author 吴展德
* @description 针对表【service_area(服务区域表)】的数据库操作Service
* @createDate 2025-12-30 10:46:05
*/
public interface ServiceAreaService extends IService<ServiceArea> {
    /**
     * 查询服务区域列表（分页）
     * @param serviceAreaPageRequest 分页查询请求参数
     * @return 分页结果
     */
    PageResult<ServiceArea> queryList(ServiceAreaPageRequest serviceAreaPageRequest);
    
    /**
     * 新增服务区域
     * @param serviceAreaRequest 服务区域请求参数
     * @return 新增的服务区域ID
     */
    Long addServiceArea(ServiceAreaRequest serviceAreaRequest);
    
    /**
     * 修改服务区域
     * @param serviceAreaRequest 服务区域请求参数
     * @return 是否修改成功
     */
    boolean updateServiceArea(ServiceAreaRequest serviceAreaRequest);
    
    /**
     * 删除服务区域
     * @param areaId 服务区域ID
     * @return 是否删除成功
     */
    boolean deleteServiceArea(Long areaId);
}
