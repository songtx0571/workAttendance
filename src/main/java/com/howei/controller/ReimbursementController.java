package com.howei.controller;

import com.alibaba.fastjson.JSON;
import com.howei.pojo.Reimbursement;
import com.howei.pojo.Users;
import com.howei.service.DepartmentService;
import com.howei.service.EmployeeService;
import com.howei.service.ReimbursementService;
import com.howei.util.DateFormat;
import com.howei.util.Page;
import com.howei.util.Result;
import com.howei.util.Type;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wa/reimbursement")
public class ReimbursementController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ReimbursementService reimbursementService;

    @Autowired
    private EmployeeService employeeService;

    /**
     * 跳转报销审核页面
     * @return
     */
    @RequestMapping("/toReimbursement")
    public String toReimbursement(){
        return "reimbursement";
    }

    public Users getPrincipal(){
        Subject subject=SecurityUtils.getSubject();
        Users users=(Users)subject.getPrincipal();
        return users;
    }

    @RequestMapping("/getReimbursementList")
    @ResponseBody
    public Result getReimbursementList(HttpServletRequest request){
        String month=request.getParameter("month");
        String departmentId=request.getParameter("departmentId");
        String page=request.getParameter("page");
        String limit=request.getParameter("limit");
        Integer financeResult=0;
        int rows=Page.getOffSet(page,limit);
        String active="";
        Map map=new HashMap();
        if(month==null&&month.equals("")){
            month=DateFormat.ThisMonth();
        }
        if(month!=null){
            map.put("month",month+"-01");
        }
        Subject subject=SecurityUtils.getSubject();
        boolean jPermitted=subject.isPermitted("报销监视员");
        boolean gPermitted=subject.isPermitted("报销管理员");
        boolean cPermitted=subject.isPermitted("报销财务员");
        boolean xPermitted=subject.isPermitted("报销项目部");
        if(gPermitted||cPermitted||jPermitted){
            if(departmentId!=null&&!departmentId.equals("")){
                map.put("departmentId",departmentId);
            }
        }else{
            Users users = this.getPrincipal();
            if (users != null && xPermitted) {
                map.put("departmentId", users.getDepartmentId());
            }
        }
        if(jPermitted||gPermitted||cPermitted){
            active=null;
        }else{
            active="1";
        }
        map.put("active",active);
        if(gPermitted){
            financeResult=1;
        }else if (cPermitted){
            financeResult=3;
        }
        map.put("financeResult",financeResult);
        List<Reimbursement> total=reimbursementService.getReimbursementList(map);
        map.put("pageSize",limit);
        map.put("page",rows);
        List<Reimbursement> list=reimbursementService.getReimbursementList(map);
        Result result=new Result();
        result.setCount(total.size());
        result.setData(list);
        result.setCode(0);
        return  result;
    }

    /**
     * 获取部门列表
     * @return
     */
    @RequestMapping("/getDepartmentMap")
    @ResponseBody
    public List<Map<String,String>> getDepartmentMap(HttpServletRequest request){
        String companyId=request.getParameter("companyId");
        Map map=new HashMap();
        String active="";
        if(companyId==null||companyId.equals("")){
            companyId="1";
        }
        Users users=this.getPrincipal();
        Subject subject=SecurityUtils.getSubject();
        boolean jPermitted=subject.isPermitted("报销监视员");
        boolean gPermitted=subject.isPermitted("报销管理员");
        boolean cPermitted=subject.isPermitted("报销财务员");
        boolean xPermitted=subject.isPermitted("报销项目部");
        //判断是否存在这四个权限
        if(!jPermitted && !gPermitted && !cPermitted && !xPermitted){
            return null;
        }
        if(xPermitted){
            if(users!=null){
                if(!gPermitted && !jPermitted && !cPermitted){
                    map.put("departmentId",users.getDepartmentId());
                }
            }
        }
        map.put("companyId",companyId);
        List<Map<String,String>> result=departmentService.getDepartmentMap(map);
        return result;
    }

    /**
     * 获取人员下拉框
     * @param request
     * @return
     */
    @RequestMapping("/getEmpMap")
    @ResponseBody
    public List<Map<String,String>> getEmpMap(HttpServletRequest request){
        String companyId=request.getParameter("companyId");
        String departmentId=request.getParameter("departmentId");
        Map map=new HashMap();
        map.put("companyId",companyId);
        map.put("departmentId",departmentId);
        List<Map<String,String>> list=employeeService.getEmpMap(map);
        return list;
    }

    /**
     * 获取公司列表
     * @return
     */
    @RequestMapping("/getCompanyMap")
    @ResponseBody
    public List<Map<String,String>> getCompanyMap(){
        List<Map<String,String>> map=departmentService.getCompanyMap();
        return map;
    }

    /**
     * 获取当前登陆人信息
     * @return
     */
    @RequestMapping("/getLoginUserName")
    @ResponseBody
    public Map<String,Object> getLoginUserName(){
        Users users=this.getPrincipal();
        Map<String,Object> map=new HashMap<>();
        if(users!=null){
            map.put("userName",users.getUserName());
        }
        return map;
    }

    /**
     * 新增报销单
     * @param reimbursement
     * @return
     */
    @RequestMapping("/addReimbursement")
    @ResponseBody
    public String  addReimbursement(@RequestBody Reimbursement reimbursement){
        Users users=this.getPrincipal();
        Integer createdBy=null;
        if(users!=null){
            createdBy=users.getEmployeeId();
            reimbursement.setReimbursor(users.getEmployeeId());
        }else{
            return JSON.toJSONString(Type.RESIGNIN);
        }
        reimbursement.setCreatedBy(createdBy);
        reimbursement.setCreated(DateFormat.getYMDHM(new Date()));
        reimbursement.setFinanceResult(0);//初始状态
        reimbursement.setStatus(0);//未审核
        reimbursementService.addReimbursement(reimbursement);
        Integer id=reimbursement.getId();
        if(id!=null){
            return JSON.toJSONString(Type.SUCCESS);
        }else {
            return JSON.toJSONString(Type.ERROR);
        }
    }

    /**
     * 审核
     * @param request
     * @return
     */
    @RequestMapping("/examine")
    @ResponseBody
    public String examine(HttpServletRequest request){
        String id=request.getParameter("id");
        String status=request.getParameter("status");
        String financeResult=request.getParameter("financeResult");//1确认有效；2确认无效；3审核通过；4审核未通过
        String financeRemark=request.getParameter("financeRemark");//审核意见
        Integer financeResultInt=Integer.parseInt(financeResult);
        if(id!=null){
            Reimbursement reimbursement=reimbursementService.getReimbursementById(id);
            if(reimbursement!=null){
                reimbursement.setFinanceResult(financeResultInt);
                //未审核状态->审核中状态
                if(status!=null&&status.equals("0")){
                    if(financeResultInt.equals(1)){//1确认有效
                        reimbursement.setStatus(1);//审核中
                    }else if(financeResultInt.equals(2)){//2确认无效
                        reimbursement.setStatus(2);//审核结束
                    }
                //审核中状态->审核结束状态
                }else if(status!=null&&status.equals("1")){
                    reimbursement.setStatus(2);//审核结束
                    reimbursement.setFinanceDate(DateFormat.getYMD());
                    reimbursement.setFinanceRemark(financeRemark);
                }
                reimbursementService.examine(reimbursement);
                return JSON.toJSONString(Type.SUCCESS);
            }
        }
        return "";
    }

    /**
     * 修改报销内容
     * @param reimbursement
     * @return
     */
    @RequestMapping("/updReimbursement")
    @ResponseBody
    public String updReimbursement(@RequestBody Reimbursement reimbursement){
        if(reimbursement.getId()!=null){
            try {
                reimbursement.setFinanceResult(0);
                reimbursement.setFinanceDate("");
                reimbursement.setStatus(0);
                reimbursementService.updReimbursement(reimbursement);
            } catch (Exception e) {
                e.printStackTrace();
                return JSON.toJSONString(Type.ERROR);
            }
            return JSON.toJSONString(Type.SUCCESS);
        }
        return JSON.toJSONString(Type.ERROR);
    }
}