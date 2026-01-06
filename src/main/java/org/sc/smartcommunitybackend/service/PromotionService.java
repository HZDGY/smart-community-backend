package org.sc.smartcommunitybackend.service;

import org.sc.smartcommunitybackend.domain.Promotion;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sc.smartcommunitybackend.dto.request.PromotionPageRequest;
import org.sc.smartcommunitybackend.dto.request.PromotionRequest;
import org.sc.smartcommunitybackend.dto.response.PageResult;
import org.sc.smartcommunitybackend.dto.response.PromotionVO;

/**
* @author 吴展德
* @description 针对表【promotion(促销活动表)】的数据库操作Service
* @createDate 2025-12-30 10:46:05
*/
public interface PromotionService extends IService<Promotion> {

    PageResult<PromotionVO> queryList(PromotionPageRequest promotionPageRequest);

    Long add(PromotionRequest promotionRequest);

    Boolean updatePromotion(Long promotionId,PromotionRequest promotionRequest);

    Boolean delete(Long promotionId);
}
