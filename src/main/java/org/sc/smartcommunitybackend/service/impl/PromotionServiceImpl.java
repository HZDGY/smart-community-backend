package org.sc.smartcommunitybackend.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import org.sc.smartcommunitybackend.common.enums.PromotionStatusEnum;
import org.sc.smartcommunitybackend.domain.Product;
import org.sc.smartcommunitybackend.domain.Promotion;
import org.sc.smartcommunitybackend.domain.PromotionProduct;
import org.sc.smartcommunitybackend.dto.request.PromotionPageRequest;
import org.sc.smartcommunitybackend.dto.request.PromotionRequest;
import org.sc.smartcommunitybackend.dto.response.AdminProductVO;
import org.sc.smartcommunitybackend.dto.response.PageResult;
import org.sc.smartcommunitybackend.dto.response.PromotionVO;
import org.sc.smartcommunitybackend.exception.BusinessException;
import org.sc.smartcommunitybackend.service.PromotionProductService;
import org.sc.smartcommunitybackend.service.PromotionService;
import org.sc.smartcommunitybackend.mapper.PromotionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author 吴展德
* @description 针对表【promotion(促销活动表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Slf4j
@Service
public class PromotionServiceImpl extends ServiceImpl<PromotionMapper, Promotion>
    implements PromotionService{
    @Resource
    private PromotionProductService promotionProductService;

    /**
     * 查询促销列表
     *
     * @param promotionPageRequest
     * @return
     */
    @Override
    public PageResult<PromotionVO> queryList(PromotionPageRequest promotionPageRequest) {
        log.info("促销列表查询参数：{}", promotionPageRequest);
        Integer pageNum = promotionPageRequest.getPageNum();
        Integer pageSize = promotionPageRequest.getPageSize();
        String promotionName = promotionPageRequest.getPromotionName();
        Integer promotionType = promotionPageRequest.getPromotionType();
        Integer status = promotionPageRequest.getStatus();
        Date startTime = promotionPageRequest.getStartTime();
        Date endTime = promotionPageRequest.getEndTime();
        String orderBy = promotionPageRequest.getOrderBy();
        Boolean isAsc = promotionPageRequest.getIsAsc();

        // 创建查询条件
        LambdaQueryWrapper<Promotion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(promotionName != null && promotionName.trim().length() > 0, Promotion::getPromotion_name, promotionName)
                .eq(promotionType != null && promotionType > 0, Promotion::getPromotion_type, promotionType)
                .eq(status != null && status > 0, Promotion::getStatus, status)
                .ge(startTime != null, Promotion::getStart_time, startTime)
                .le(endTime != null, Promotion::getEnd_time, endTime);
        queryWrapper.orderBy(orderBy != null && orderBy.trim().length() > 0, isAsc, Promotion::getCreate_time);
        queryWrapper.orderByDesc(orderBy != null && orderBy.trim().length() > 0, Promotion::getUpdate_time);
        queryWrapper.orderByDesc(Promotion::getCreate_time);
        // 设置默认分页参数
        pageNum = (pageNum != null && pageNum > 0) ? pageNum : 1;
        pageSize = (pageSize != null && pageSize > 0) ? pageSize : 10;

        // 创建分页对象
        Page<Promotion> page = new Page<>(pageNum, pageSize);

        // 执行分页查询
        Page<Promotion> resultPage = this.page(page, queryWrapper);

        log.info("分页查询结果：{}", resultPage);
        // 转换结果
        List<Promotion> records = resultPage.getRecords();
        List<PromotionVO> promotionVOList = records.stream().map(record -> {
            //根据促销ID查询绑定的商品ID
            List<Long> productIds = promotionProductService.list(new QueryWrapper<PromotionProduct>().eq("promotion_id", record.getPromotion_id())).stream().map(PromotionProduct::getProduct_id).toList();
            if (productIds == null || productIds.isEmpty() || productIds.size() <= 0) {
                log.warn("促销ID为{}的促销没有绑定商品", record.getPromotion_id());
                productIds = new ArrayList<>();
            }
            PromotionVO promotionVO = new PromotionVO();
            promotionVO.setPromotionId(record.getPromotion_id());
            promotionVO.setPromotionName(record.getPromotion_name());
            promotionVO.setPromotionType(String.valueOf(record.getPromotion_type()));
            promotionVO.setStartTime(record.getStart_time());
            promotionVO.setEndTime(record.getEnd_time());
            promotionVO.setProductIds(productIds);
            return promotionVO;
        }).toList();
        // 创建分页结果对象
        PageResult<PromotionVO> pageResult = new PageResult<>();
        pageResult.setList(promotionVOList);
        pageResult.setTotal(resultPage.getTotal());
        pageResult.setPages(resultPage.getPages());
        return pageResult;
    }

    /**
     * 添加促销
     *
     * @param promotionRequest
     * @return
     */
    @Override
    public Long add(PromotionRequest promotionRequest) {
        log.info("添加促销参数：{}", promotionRequest);
        String promotionName = promotionRequest.getPromotionName();
        String promotionType = promotionRequest.getPromotionType();
        Date startTime = promotionRequest.getStartTime();
        Date endTime = promotionRequest.getEndTime();
        List<Long> productIds = promotionRequest.getProductIds();
        if (promotionName == null || promotionName.trim().length() <= 0) {
            throw new BusinessException("促销名称不能为空");
        }
        if (promotionType == null || promotionType.trim().length() <= 0) {
            throw new BusinessException("促销类型不能为空");
        }
        if (startTime == null || startTime.before(DateTime.now())) {
            throw new BusinessException("开始时间不能为空");
        }
        if (endTime == null || endTime.before(startTime)) {
            throw new BusinessException("结束时间不能为空");
        }
        Promotion promotion = new Promotion();
        promotion.setPromotion_name(promotionName);
        promotion.setPromotion_type(Integer.parseInt(promotionType));
        promotion.setStart_time(startTime);
        promotion.setEnd_time(endTime);
        promotion.setCreate_time(new Date());
        int insert = baseMapper.insert(promotion);
        if (insert <= 0) {
            throw new BusinessException("添加促销失败");
        }
        return promotion.getPromotion_id();
    }

    /**
     * 修改促销
     *
     * @param promotionRequest
     * @return
     */
    @Transactional
    @Override
    public Boolean updatePromotion(Long promotionId,PromotionRequest promotionRequest) {
        log.info("修改促销参数：{}", promotionRequest);

        String promotionName = promotionRequest.getPromotionName();
        String promotionType = promotionRequest.getPromotionType();
        Date startTime = promotionRequest.getStartTime();
        Date endTime = promotionRequest.getEndTime();
        List<Long> productIds = promotionRequest.getProductIds();
        if (promotionId == null || promotionId <= 0){
            throw new BusinessException("促销ID不能为空");
        }
        Promotion promotion = baseMapper.selectById(promotionId);
        if (promotion == null){
            throw new BusinessException("促销不存在");
        }
        if (Objects.equals(promotion.getStatus(), PromotionStatusEnum.VALID.getCode())){
            throw new BusinessException("促销正在生效中，请勿修改");
        }
        boolean update = lambdaUpdate().eq(Promotion::getPromotion_id, promotionId)
                .set(promotionName != null && promotionName.trim().length() > 0, Promotion::getPromotion_name, promotionName)
                .set(promotionType != null && promotionType.trim().length() > 0, Promotion::getPromotion_type, promotionType)
                .set(startTime != null && startTime.before(DateTime.now()), Promotion::getStart_time, startTime)
                .set(endTime != null && endTime.before(startTime), Promotion::getEnd_time, endTime)
                .update();
        if (!update) {
            throw new BusinessException("修改促销失败");
        }
        promotionProductService.updateByPromotionId(promotionId, productIds);
        return update;
    }

    /**
     * 删除促销
     *
     * @param promotionId
     * @return
     */
    @Override
    public Boolean delete(Long promotionId) {
        log.info("删除促销参数：{}", promotionId);
        if (promotionId == null || promotionId <= 0){
            throw new BusinessException("促销ID不能为空");
        }
        Promotion promotion = baseMapper.selectById(promotionId);
        if (promotion == null){
            throw new BusinessException("促销不存在");
        }
        if (Objects.equals(promotion.getStatus(), PromotionStatusEnum.VALID.getCode())){
            throw new BusinessException("促销正在生效中，请勿删除");
        }
        boolean delete = removeById(promotionId);
        if (!delete) {
            throw new BusinessException("删除促销失败");
        }
        return delete;
    }
}




