package com.howei.controller;

import com.alibaba.fastjson.JSON;
import com.howei.pojo.*;
import com.howei.service.WageBaseService;
import com.howei.util.DateFormat;
import com.howei.util.Page;
import com.howei.util.Result;
import com.howei.util.Type;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("wa/wagebase")
public class WageBaseController {

    @Autowired
    private WageBaseService wageBaseService;

    /**
     * 跳转工资岗位配置页面
     * @return
     */
    @RequestMapping("/toPostWages")
    public ModelAndView toDefect(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("postWages");
        return modelAndView;
    }

    /**
     * 获取实体类信息
     * @return
     */
    public Users getPrincipal(){
        Subject subject=SecurityUtils.getSubject();
        Users users=(Users) subject.getPrincipal();
        return users;
    }


    /**
     * 获取权限信息
     * @return
     */
    public Subject getSubject(){
        Subject subject=SecurityUtils.getSubject();
        return subject;
    }

    /**--------------------------------岗位设置---------------------------------*/

    /**
     * 查询薪资岗位列表
     * @param request
     * @return
     */
    @RequestMapping("/getWagesPostList")
    public Result getPostList(HttpServletRequest request){
        String page = request.getParameter("page");
        String pageSize = request.getParameter("limit");
        int rows = Page.getOffSet(page, pageSize);
        Map map = new HashMap();
        map.put("page", rows);
        map.put("pageSize", pageSize);
        List<WagesPost> list=wageBaseService.getWagesPostList(map);
        Result result=new Result();
        result.setCode(0);
        result.setData(list);
        return result;
    }

    /**
     * 添加岗位
     * @return
     */
    @PostMapping("/addPost")
    @ResponseBody
    public String addPost(@RequestBody WagesPost wagesPost){
        wagesPost.setCompanyId(1);
        wagesPost.setIsactive(1);
        WagesPost wp=wageBaseService.findWagesPostByName(wagesPost.getName());
        if(wp==null||wp.getId()==0){
            wageBaseService.addWagesPost(wagesPost);
            return JSON.toJSONString(Type.SUCCESS);
        }else{
            return JSON.toJSONString(Type.havaRecord);//已存在此纪录
        }
    }

    /**
     * 修改薪资岗位
     * @return
     */
    @RequestMapping("/updPost")
    @ResponseBody
    public String updPost(@RequestBody WagesPost wagesPost){
        if(wagesPost.getIndex()==1){//修改名称
            WagesPost wp=wageBaseService.findWagesPostByName(wagesPost.getName());
            if(wp!=null){
                return JSON.toJSONString(Type.havaRecord);
            }
        }
        try {
            wageBaseService.updWagesPost(wagesPost);
        } catch (Exception e) {
            return JSON.toJSONString(Type.ERROR);
        }
        return JSON.toJSONString(Type.SUCCESS);
    }


    /**--------------------------------岗位的等级设置---------------------------------*/

    /**
     * 获取列表
     * @param wagesPostId
     * @return
     */
    @RequestMapping("getPostGradeList")
    public Result getPostGradeList(@RequestParam("wagesPostId") Integer wagesPostId){
        Result result=new Result();

        Map map=new HashMap();
        if(wagesPostId!=null){
            map.put("wagesPostId",wagesPostId);
        }

        List<PostGrade> list= null;
        try {
            list = wageBaseService.getPostGradeList(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setCode(0);
        result.setData(list);
        return result;

    }

    /**
     * 添加岗位等级
     * @param postGrade
     * @return
     */
    @PostMapping("addPostGrade")
    public String addPostGrade(@RequestBody PostGrade postGrade){
        if(postGrade==null){
            return Type.CANCEL.toString();
        }
        //判断是否存在同名等级
        int count=wageBaseService.findPostGradeByName(postGrade.getWagesPostId(),postGrade.getGradeName());
        if(count>0){
            return Type.havaRecord.toString();
        }

        postGrade.setCreated(DateFormat.getYMD());
        //添加
        wageBaseService.addPostGrade(postGrade);
        if(postGrade.getId()!=null){
            return Type.SUCCESS.toString();
        }
        return Type.ERROR.toString();
    }

    /**
     * 修改岗位等级
     * @param postGrade
     * @return
     */
    @PostMapping("updPostGrade")
    public String updPostGrade(@RequestBody PostGrade postGrade){
        if(postGrade==null){
            return Type.CANCEL.toString();
        }
        //判断是否存在同名等级
        if(postGrade.getIndex()==1){
            int count=wageBaseService.findPostGradeByName(postGrade.getWagesPostId(),postGrade.getGradeName());
            if(count>0){
                return Type.havaRecord.toString();
            }
        }
        postGrade.setCreated(DateFormat.getYMD());
        wageBaseService.updPostGrade(postGrade);
        if(postGrade.getId()!=null){
            return Type.SUCCESS.toString();
        }
        return Type.ERROR.toString();
    }

}
