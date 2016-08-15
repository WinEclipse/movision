package com.zhuhuibao.business.mall;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.fsearch.utils.StringUtil;
import com.zhuhuibao.mybatis.memCenter.entity.CertificateRecord;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.entity.SuccessCase;
import com.zhuhuibao.mybatis.memCenter.service.BrandService;
import com.zhuhuibao.mybatis.memCenter.service.MemShopService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.memCenter.service.SuccessCaseService;
import com.zhuhuibao.mybatis.product.service.ProductService;
import com.zhuhuibao.mybatis.vip.entity.VipMemberInfo;
import com.zhuhuibao.mybatis.vip.service.VipInfoService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商城商户主页
 * Created by cxx on 2016/6/22 0022.
 */
@RestController
@RequestMapping("/rest/mall/site")
public class CompanyInfoController {
    private static final Logger log = LoggerFactory.getLogger(CompanyInfoController.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SuccessCaseService successCaseService;

    @Autowired
    private BrandService brandService;

    @Autowired
    MemShopService memShopService;

    @Autowired
    VipInfoService vipInfoService;

    @ApiOperation(value = "商户主页相关信息", notes = "商户主页相关信息")
    @RequestMapping(value = "sel_index_companyInfo", method = RequestMethod.GET)
    public Response companyInfo(@ApiParam(value = "商户id")@RequestParam String id)  {

        //查询公司信息
        Member member = memberService.findMemById(id);

        //查询公司vip信息
        VipMemberInfo vip = vipInfoService.findVipMemberInfoById(Long.parseLong(id));

        //查询公司产品类别
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("status", Constants.product_status_publish);
        queryMap.put("createid",id);
        List<Map<String,String>> productTypeList = productService.queryProductTypeListByCompanyId(queryMap);

        //页面展示
        Map map = new HashMap();
        if(vip!=null){
            map.put("vipLevel",vip.getVipLevel());
        }else {
            map.put("vipLevel",100);
        }

        map.put("logo",member.getEnterpriseLogo());
        map.put("companyName",member.getEnterpriseName());
        map.put("webSite",member.getEnterpriseWebSite());
        String provinceName = "";
        if(!StringUtils.isEmpty(member.getProvinceName())){
            provinceName = member.getProvinceName();
        }
        String cityName = "";
        if(!StringUtils.isEmpty(member.getCityName())){
            cityName = member.getCityName();
        }
        String areaName = "";
        if(!StringUtils.isEmpty(member.getAreaName())){
            areaName = member.getAreaName();
        }
        if(member.getProvince()!=null){
            map.put("address",provinceName + cityName + areaName + member.getAddress());
        }else {
            map.put("address","");
        }

        map.put("telephone",member.getEnterpriseTelephone());
        map.put("fax",member.getEnterpriseFox());
        map.put("introduce",member.getEnterpriseDesc());
        map.put("productTypeList",productTypeList);

        return new Response(map);
    }

    @ApiOperation(value = "热销商品", notes = "热销商品")
    @RequestMapping(value = "sel_company_hot_product", method = RequestMethod.GET)
    public Response sel_company_hot_product(@ApiParam(value = "商户id")@RequestParam String id,
                                            @ApiParam(value = "条数")@RequestParam int count)  {

        //查询公司热销产品
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("status", Constants.product_status_publish);
        queryMap.put("createid",id);
        queryMap.put("count",count);
        List<Map<String,String>> productList = productService.queryHotProductListByCompanyId(queryMap);

        return new Response(productList);
    }

    @ApiOperation(value = "最新供应商品", notes = "最新供应商品")
    @RequestMapping(value = "sel_company_latest_product", method = RequestMethod.GET)
    public Response sel_company_latest_product(@ApiParam(value = "商户id")@RequestParam String id,
                                            @ApiParam(value = "条数")@RequestParam int count)  {

        //查询公司最新供应商品
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("status", Constants.product_status_publish);
        queryMap.put("createid",id);
        queryMap.put("count",count);
        List<Map<String,String>> productList = productService.queryLatestProductListByCompanyId(queryMap);

        return new Response(productList);
    }

    @ApiOperation(value = "优秀案例", notes = "优秀案例")
    @RequestMapping(value = "sel_company_great_case", method = RequestMethod.GET)
    public Response sel_company_great_case(@ApiParam(value = "商户id")@RequestParam String id,
                                               @ApiParam(value = "条数")@RequestParam int count)  {

        //查询公司优秀案例
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("status", "1");
        queryMap.put("is_deleted", "0");
        queryMap.put("createid",id);
        queryMap.put("count",count);
        List<Map<String,String>> caseList = successCaseService.queryGreatCaseListByCompanyId(queryMap);

        return new Response(caseList);
    }

    @ApiOperation(value = "公司介绍", notes = "公司介绍")
    @RequestMapping(value = "sel_company_introduce", method = RequestMethod.GET)
    public Response sel_company_introduce(@ApiParam(value = "商户id")@RequestParam String id)  {

        //查询公司信息
        Member member = memberService.findMemById(id);

        //查询公司产品类别
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("status", Constants.product_status_publish);
        queryMap.put("createid",id);
        List<Map<String,String>> productTypeList = productService.queryProductTypeListByCompanyId(queryMap);

        //页面展示
        Map map = new HashMap();
        map.put("companyName",member.getEnterpriseName());
        if("1".equals(member.getCurrency())){
            map.put("registerCapital",member.getRegisterCapital()+"万元");
        }else{
            map.put("registerCapital",member.getRegisterCapital()+"万美元");
        }
        if(member.getEnterpriseProvince()!=null){
            map.put("address",member.getEnterpriseProvinceName()+member.getEnterpriseCityName()+member.getEnterpriseAreaName());
        }else {
            map.put("address","");
        }

        map.put("companyType",member.getEnterpriseTypeName());
        map.put("createTime",member.getEnterpriseCreaterTime());
        map.put("introduce",member.getEnterpriseDesc());
        map.put("saleRange",member.getSaleProductDesc());
        map.put("productTypeList",productTypeList);

        return new Response(map);
    }

    @ApiOperation(value = "公司联系方式", notes = "公司联系方式")
    @RequestMapping(value = "sel_company_contact", method = RequestMethod.GET)
    public Response sel_company_contact(@ApiParam(value = "商户id")@RequestParam String id)  {

        //查询公司信息
        Member member = memberService.findMemById(id);

        //页面展示
        Map map = new HashMap();
        map.put("companyName",member.getEnterpriseName());
        map.put("webSite",member.getEnterpriseWebSite());
        if(member.getProvince()!=null){
            map.put("address",member.getProvinceName()+member.getCityName()+member.getAreaName()+member.getAddress());
        }else {
            map.put("address","");
        }
        map.put("telephone",member.getEnterpriseTelephone());
        map.put("fax",member.getEnterpriseFox());

        return new Response(map);
    }

    @ApiOperation(value = "公司荣誉资质", notes = "公司荣誉资质")
    @RequestMapping(value = "sel_company_certificate", method = RequestMethod.GET)
    public Response sel_company_certificate(@ApiParam(value = "商户id")@RequestParam String id)  {

        CertificateRecord certificateRecord = new CertificateRecord();
        certificateRecord.setMem_id(id);
        //供应商资质
        certificateRecord.setType("1");
        certificateRecord.setIs_deleted(0);
        //审核通过
        certificateRecord.setStatus("1");
        List<CertificateRecord> certificateRecordList = memberService.certificateSearch(certificateRecord);

        return new Response(certificateRecordList);
    }

    @ApiOperation(value = "公司成功案例（分页）", notes = "公司成功案例（分页）")
    @RequestMapping(value = "sel_company_success_caseList", method = RequestMethod.GET)
    public Response sel_company_success_caseList(@ApiParam(value = "商户id")@RequestParam String id,
                                                 @RequestParam(required = false,defaultValue = "1") String pageNo,
                                                 @RequestParam(required = false,defaultValue = "10") String pageSize)  {

        Paging<Map<String,String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));

        //查询公司优秀案例
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("status", "1");
        queryMap.put("createid",id);
        List<Map<String,String>> caseList = successCaseService.findAllSuccessCaseList(pager,queryMap);
        pager.result(caseList);

        return new Response(pager);
    }

    @ApiOperation(value = "成功案例详情", notes = "成功案例详情")
    @RequestMapping(value = "sel_company_success_case", method = RequestMethod.GET)
    public Response sel_company_success_case(@ApiParam(value = "案例id")@RequestParam String id)  {
        SuccessCase successCase = successCaseService.querySuccessCaseById(id);
        //点击率加1
        successCase.setViews(String.valueOf(Integer.parseInt(successCase.getViews())+1));
        successCaseService.updateSuccessCase(successCase);
        return new Response(successCase);
    }

    @ApiOperation(value = "公司产品（分页）", notes = "公司产品（分页）")
    @RequestMapping(value = "sel_company_product_list", method = RequestMethod.GET)
    public Response sel_company_product_list(@ApiParam(value = "产品类别id")@RequestParam(required = false) String fcateid,
                                             @ApiParam(value = "商户id")@RequestParam String id,
                                             @RequestParam(required = false,defaultValue = "1") String pageNo,
                                             @RequestParam(required = false,defaultValue = "10") String pageSize)  {

        Paging<Map<String,String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));

        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("status", "1");
        queryMap.put("createid",id);
        queryMap.put("fcateid",fcateid);
        List<Map<String,String>> productList = productService.findAllProductListByProductType(pager,queryMap);
        pager.result(productList);

        return new Response(pager);
    }


    @ApiOperation(value = "优秀厂商,代理商,渠道商", notes = "优秀厂商,代理商,渠道商")
    @RequestMapping(value = "sel_great_company", method = RequestMethod.GET)
    public Response sel_great_manufacturer(@ApiParam(value="频道类型 3：商城")@RequestParam String chanType,
                                           @ApiParam(value="频道下子页面.manufacturer:厂商;channel:渠道商;agent:代理商") @RequestParam String page,
                                           @ApiParam(value="广告所在区域:F1:优秀厂商,渠道商,代理商") @RequestParam String advArea)  {
        Map<String,Object> map = new HashMap<>();
        map.put("chanType",chanType);
        map.put("page",page);
        map.put("advArea",advArea);
        List<Map<String,String>> list = memberService.queryGreatCompany(map);
        return new Response(list);
    }

    @ApiOperation(value = "热门产品", notes = "热门产品")
    @RequestMapping(value = "sel_hot_product", method = RequestMethod.GET)
    public Response sel_hot_product(@ApiParam(value="频道类型 3：商城")@RequestParam String chanType,
                                    @ApiParam(value="频道下子页面.index:首页;manufacturer:厂商;channel:渠道商") @RequestParam String page,
                                    @ApiParam(value="广告所在区域:F2:厂商渠道商热门产品;F10:首页十楼（热门产品)") @RequestParam String advArea)  {
        Map<String,Object> map = new HashMap<>();
        map.put("chanType",chanType);
        map.put("page",page);
        map.put("advArea",advArea);
        List<Map<String,String>> list = productService.queryHotProduct(map);
        return new Response(list);
    }

    @ApiOperation(value = "推荐品牌", notes = "推荐品牌")
    @RequestMapping(value = "sel_recommend_brand", method = RequestMethod.GET)
    public Response sel_recommend_brand(@ApiParam(value="频道类型 3：商城")@RequestParam String chanType,
                                    @ApiParam(value="频道下子页面.agent:代理商") @RequestParam String page,
                                    @ApiParam(value="广告所在区域:F2:推荐品牌") @RequestParam String advArea)  {
        Map<String,Object> map = new HashMap<>();
        map.put("chanType",chanType);
        map.put("page",page);
        map.put("advArea",advArea);
        List<Map<String,String>> list = brandService.queryRecommendBrand(map);
        return new Response(list);
    }

    @ApiOperation(value = "获取商铺banner及名称", notes = "获取商铺banner及名称")
    @RequestMapping(value = "sel_shop_banner", method = RequestMethod.GET)
    public Response sel_shop_banner(@ApiParam(value = "商户id")@RequestParam String id)  {
        Map<String,String> map = memShopService.queryShopBanner(id);
        return new Response(map);
    }
}
