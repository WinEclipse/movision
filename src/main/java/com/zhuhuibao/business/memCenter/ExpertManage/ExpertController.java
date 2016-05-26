package com.zhuhuibao.business.memCenter.ExpertManage;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.*;
import com.zhuhuibao.mybatis.memCenter.service.ExpertService;
import com.zhuhuibao.shiro.realm.OMSRealm;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 专家会员中心接口管理
 * Created by cxx on 2016/5/17 0017.
 */
@RestController
@RequestMapping("/rest/expert")
@Api(value = "expert", description = "会员中心-专家")
public class ExpertController {
    private static final Logger log = LoggerFactory.getLogger(ExpertController.class);

    @Autowired
    private ExpertService expertService;

    @ApiOperation(value = "我的技术成果(后台)", notes = "我的技术成果(后台)", response = Response.class)
    @RequestMapping(value = "myAchievementList", method = RequestMethod.GET)
    public Response myAchievementList(@ApiParam(value = "标题") @RequestParam(required = false) String title,
                                      @ApiParam(value = "状态") @RequestParam(required = false) String status,
                                      @RequestParam(required = false) String pageNo,
                                      @RequestParam(required = false) String pageSize) {
        Response response = new Response();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Achievement> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<>();
        //查询传参
        map.put("title", title);
        map.put("status", status);
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if (null != principal) {
                map.put("createId", principal.getId());
                List<Achievement> achievementList = expertService.findAllAchievementList(pager, map);
                List list = new ArrayList();
                for(int i=0;i<achievementList.size();i++){
                    Achievement achievement = achievementList.get(i);
                    Map m = new HashMap();
                    m.put("id",achievement.getId());
                    m.put("title",achievement.getTitle());
                    m.put("systemName",achievement.getSystemName());
                    m.put("useAreaName",achievement.getUseAreaName());
                    m.put("updateTime",achievement.getUpdateTime());
                    m.put("status",achievement.getStatus());
                    list.add(m);
                }
                pager.result(list);
                response.setData(pager);
            } else {
                throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        } else {
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "删除技术成果", notes = "删除技术成果", response = Response.class)
    @RequestMapping(value = "deleteAchievement", method = RequestMethod.POST)
    public Response deleteAchievement(@ApiParam(value = "技术成果ids,逗号隔开") @RequestParam String ids)
            {
        Response response = new Response();
        String[] idList = ids.split(",");
        for (String id : idList) {
            String is_deleted = "1";
            Achievement achievement = new Achievement();
            achievement.setIs_deleted(is_deleted);
            achievement.setId(id);
            expertService.updateAchievement(achievement);
        }
        return response;
    }

    @ApiOperation(value = "更新技术成果", notes = "更新技术成果", response = Response.class)
    @RequestMapping(value = "updateAchievement", method = RequestMethod.POST)
    public Response updateAchievement(@ModelAttribute Achievement achievement)  {
        Response response = new Response();
        expertService.updateAchievement(achievement);
        return response;
    }

    @ApiOperation(value = "发布协会动态", notes = "发布协会动态", response = Response.class)
    @RequestMapping(value = "publishDynamic", method = RequestMethod.POST)
    public Response publishDynamic(@ModelAttribute Dynamic dynamic)  {
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        //判断是否登陆
        if(null != session) {
            if ("1".equals(dynamic.getCreaterType())) {
                OMSRealm.ShiroOmsUser principal = (OMSRealm.ShiroOmsUser) session.getAttribute("oms");
                if (null != principal) {
                    dynamic.setCreateId(principal.getId().toString());
                    expertService.publishDynamic(dynamic);
                } else {
                    throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                }
            }else{
                ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
                if(null != principal){
                    dynamic.setCreateId(principal.getId().toString());
                    expertService.publishDynamic(dynamic);
                }else{
                    throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                }
            }
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "删除协会动态", notes = "删除协会动态", response = Response.class)
    @RequestMapping(value = "deleteDynamic", method = RequestMethod.POST)
    public Response deleteDynamic(@ApiParam(value = "协会动态ids,逗号隔开") @RequestParam String ids)  {
        Response response = new Response();
        String[] idList = ids.split(",");
        for (String id : idList) {
            String is_deleted = "1";
            Dynamic dynamic = new Dynamic();
            dynamic.setIs_deleted(is_deleted);
            dynamic.setId(id);
            expertService.updateDynamic(dynamic);
        }
        return response;
    }

    @ApiOperation(value = "更新协会动态", notes = "更新协会动态", response = Response.class)
    @RequestMapping(value = "updateDynamic", method = RequestMethod.POST)
    public Response updateDynamic(@ModelAttribute Dynamic dynamic)  {
        Response response = new Response();
        expertService.updateDynamic(dynamic);
        return response;
    }

    @ApiOperation(value = "我的协会动态(后台)", notes = "我的协会动态(后台)", response = Response.class)
    @RequestMapping(value = "myDynamicList", method = RequestMethod.GET)
    public Response myDynamicList(@ApiParam(value = "标题") @RequestParam(required = false) String title,
                                  @ApiParam(value = "状态") @RequestParam(required = false) String status,
                                  @RequestParam(required = false) String pageNo,
                                  @RequestParam(required = false) String pageSize)  {
        Response response = new Response();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Dynamic> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<>();
        //查询传参
        map.put("title", title);
        map.put("status", status);
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if (null != principal) {
                map.put("createId", principal.getId());
                List<Dynamic> dynamicList = expertService.findAllDynamicList(pager, map);
                List list = new ArrayList();
                for(int i=0;i<dynamicList.size();i++){
                    Dynamic Dynamic = dynamicList.get(i);
                    Map m = new HashMap();
                    m.put("id",Dynamic.getId());
                    m.put("title",Dynamic.getTitle());
                    m.put("updateTime",Dynamic.getUpdateTime());
                    m.put("status",Dynamic.getStatus());
                    list.add(m);
                }
                pager.result(list);
                response.setData(pager);
            } else {
                throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        } else {
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "更新专家信息", notes = "更新专家信息", response = Response.class)
    @RequestMapping(value = "updateExpert", method = RequestMethod.POST)
    public Response updateExpert(@ModelAttribute Expert expert)  {
        Response response = new Response();
        expertService.updateExpert(expert);
        return response;
    }

    @ApiOperation(value = "根据id查询专家全部信息", notes = "根据id查询专家全部信息", response = Response.class)
    @RequestMapping(value = "queryExpertById", method = RequestMethod.GET)
    public Response queryExpertById(@ApiParam(value = "专家id") @RequestParam String id)  {
        Response response = new Response();
        Expert expert = expertService.queryExpertById(id);
        response.setData(expert);
        return response;
    }

    @ApiOperation(value = "查询等我回答的問題列表", notes = "查询专家頁面等我回答的問題列表", response = Response.class)
    @RequestMapping(value = "queryExpertQuestion", method = RequestMethod.GET)
    public Response queryExpertQuestion(@RequestParam(required = false) String pageNo,
                                        @RequestParam(required = false) String pageSize)  {
        Response response = new Response();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String,String>> pager = new Paging<Map<String,String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<>();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if (null != principal) {
                map.put("id",principal.getId());
                List<Map<String,String>> questionList = expertService.queryExpertQuestion(pager,map);
                pager.result(questionList);
                response.setData(pager);
            }else {
                throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "立刻回答", notes = "立刻回答", response = Response.class)
    @RequestMapping(value = "answerQuestion", method = RequestMethod.POST)
    public Response answerQuestion(@ModelAttribute Answer answer)  {
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if (null != principal) {
                answer.setCreateid(principal.getId().toString());
                expertService.answerQuestion(answer);
            }else {
                throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "查询我(專家)已經回答的問題列表", notes = "查询我(專家)已經回答的問題列表", response = Response.class)
    @RequestMapping(value = "queryMyAnswerQuestion", method = RequestMethod.GET)
    public Response queryMyAnswerQuestion(@RequestParam(required = false) String pageNo,
                                        @RequestParam(required = false) String pageSize)  {
        Response response = new Response();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String,String>> pager = new Paging<Map<String,String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<>();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if (null != principal) {
                map.put("id",principal.getId());
                List<Map<String,String>> questionList = expertService.queryMyAnswerQuestion(pager,map);
                pager.result(questionList);
                response.setData(pager);
            }else {
                throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "查询我提問的問題列表", notes = "查询我提問的問題列表", response = Response.class)
    @RequestMapping(value = "queryMyQuestion", method = RequestMethod.GET)
    public Response queryMyQuestion(@RequestParam(required = false) String pageNo,
                                          @RequestParam(required = false) String pageSize)  {
        Response response = new Response();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String,String>> pager = new Paging<Map<String,String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<>();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if (null != principal) {
                map.put("id",principal.getId());
                List<Map<String,String>> questionList = expertService.queryMyQuestion(pager,map);
                pager.result(questionList);
                response.setData(pager);
            }else {
                throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "查询我提問的一條問題及其回答內容", notes = "查询我提問的一條問題及其回答內容", response = Response.class)
    @RequestMapping(value = "queryMyQuestionById", method = RequestMethod.GET)
    public Response queryMyQuestionById(@ApiParam(value = "問題id")@RequestParam String id)  {
        Response response = new Response();
        Map map = expertService.queryMyQuestionById(id);
        response.setData(map);
        return response;
    }

    @ApiOperation(value = "关闭问题", notes = "关闭问题", response = Response.class)
    @RequestMapping(value = "closeQuestion", method = RequestMethod.POST)
    public Response closeQuestion(@ApiParam(value = "問題id")@RequestParam String id)  {
        Response response = new Response();
        Question question = new Question();
        question.setId(id);
        //狀態設為已關閉
        question.setStatus("2");
        expertService.updateQuestionInfo(question);
        return response;
    }

    @ApiOperation(value = "采纳答案", notes = "采纳答案", response = Response.class)
    @RequestMapping(value = "acceptAnswer", method = RequestMethod.POST)
    public Response acceptAnswer(@ApiParam(value = "問題id")@RequestParam String questionId,
                                 @ApiParam(value = "答案id")@RequestParam String answerId)  {
        Response response = new Response();
        Question question = new Question();
        question.setId(questionId);
        //設置採納答案id
        question.setAnswerId(answerId);
        //狀態設為已關閉
        question.setStatus("2");
        expertService.updateQuestionInfo(question);
        return response;
    }
}
