package com.movision.facade.Goods;

import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.goods.entity.GoodsDetail;
import com.movision.mybatis.goods.entity.GoodsImg;
import com.movision.mybatis.goods.service.GoodsService;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessment;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessmentVo;
import com.movision.mybatis.goodsAssessmentImg.entity.GoodsAssessmentImg;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/2/15 19:04
 */
@Service
public class GoodsFacade {

    @Autowired
    private GoodsService goodsService;

    /**
     * 根据商品id查询商品详情
     *
     * @param goodsid
     * @return
     */
    public GoodsDetail queryGoodDetail(String goodsid) {

        //首先查询用户基本信息
        GoodsDetail goodsDetail = goodsService.queryGoodDetail(Integer.parseInt(goodsid));

        //再查询用户商品实物图列表集合
        List<GoodsImg> goodsImgList = goodsService.queryGoodsImgList(Integer.parseInt(goodsid));
        goodsDetail.setGoodsImgList(goodsImgList);

        return goodsDetail;
    }

    public List<GoodsAssessmentVo> queryGoodsAssessment(String pageNo, String pageSize, String goodsid) {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<GoodsAssessmentVo> pager = new Paging<GoodsAssessmentVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));

        List<GoodsAssessmentVo> goodsAssessmentList = goodsService.queryGoodsAssessment(pager, Integer.parseInt(goodsid));

        for (int i = 0; i < goodsAssessmentList.size(); i++) {
            //如果官方评论是针对某个评论回复的，查询父评论信息
            if (goodsAssessmentList.get(i).getPid() != null) {
                goodsAssessmentList.get(i).setGoodsAssessmentVo(goodsService.queryPassessment(goodsAssessmentList.get(i).getPid()));
            }
            //如果评论是有图的，查询评价图片列表
            if (goodsAssessmentList.get(i).getIsimage() == 1) {
                List<GoodsAssessmentImg> goodsAssessmentImgList = goodsService.queryGoodsAssessmentImg(goodsAssessmentList.get(i).getId());
                goodsAssessmentList.get(i).setGoodsAssessmentImgList(goodsAssessmentImgList);
            }
        }

        return goodsAssessmentList;
    }
}
