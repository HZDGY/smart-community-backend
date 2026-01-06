package org.sc.smartcommunitybackend.dto.request;

import lombok.Data;

import java.util.List;

/**
 * 更新图片排序请求 DTO
 */
@Data
public class UpdateImageOrderRequest {
    
    /**
     * 图片排序列表
     */
    private List<ImageOrder> imageOrders;
    
    @Data
    public static class ImageOrder {
        /**
         * 图片ID
         */
        private Long imageId;
        
        /**
         * 排序顺序
         */
        private Integer sortOrder;
    }
}
