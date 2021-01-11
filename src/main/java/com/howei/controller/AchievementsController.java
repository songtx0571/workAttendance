package com.howei.controller;

import com.alibaba.fastjson.JSON;
import com.howei.pojo.*;
import com.howei.service.BehaviorService;
import com.howei.service.EmployeeService;
import com.howei.service.PerformanceService;
import com.howei.util.DateFormat;
import com.howei.util.Page;
import com.howei.util.Result;
import com.howei.util.Type;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.shiro.authz.annotation.Logical.OR;

/**
 * 绩效
 */
@Controller
@RequestMapping("/wa/achievements")
//@CrossOrigin(origins="http://test.hopeop.com:80",allowCredentials = "true")
public class AchievementsController {

    @Autowired
    private BehaviorService behaviorService;

    @Autowired
    private PerformanceService performanceService;

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/toAchievements")
    public String toAchievements(){
        return "achievements";
    }

    public Users getPrincipal(){
        Subject subject=SecurityUtils.getSubject();
        Users users=(Users) subject.getPrincipal();
        return users;
    }

    /**
     * 查询所有员工业绩
     * @param request
     * @return
     */
    @RequiresPermissions(value = {"绩效查询"},logical = OR)
    @RequestMapping("/getAssessment")
    @ResponseBody
    public String getAssessment(HttpServletRequest request){
        String cycle = request.getParameter("cycle");
        String empIdStr="";
        Users users=this.getPrincipal();
        if(users!=null){
            empIdStr+=users.getEmployeeId()+",";
            List<Employee> rootList=employeeService.getEmployeeByManager(users.getEmployeeId());
            if(rootList!=null){
                List<Employee> empList=employeeService.getEmployeeByManager(0);
                for(Employee employee:rootList){
                    empIdStr+=employee.getId()+",";
                    empIdStr+=getUsersId(employee.getId(),empList);
                }
            }
        }else{//当用户session为空，默认为管理员权限
            List<Employee> rootList=employeeService.getEmployeeByManager(240);
            if(rootList!=null){
                List<Employee> empList=employeeService.getEmployeeByManager(0);
                for(Employee employee:rootList){
                    empIdStr+=employee.getId()+",";
                    empIdStr+=getUsersId(employee.getId(),empList);
                }
            }
        }
        if(empIdStr!=null&&!empIdStr.equals("")){
            empIdStr=empIdStr.substring(0,empIdStr.lastIndexOf(","));
        }
        if(cycle==null||cycle.equals("")){
            cycle=DateFormat.beforeMonth();
        }
        Map map=new HashMap();
        map.put("cycle",cycle);
        map.put("empId",empIdStr);
        List<Assessment> list= behaviorService.getAssessment(map);
        Result result=new Result();
        result.setData(list);
        result.setCount(list.size());
        result.setCode(0);
        result.setMsg("success");
        return JSON.toJSONString(result);
    }

    /**
     * 查询员工业绩:所有被绩效管理的员工
     * @param empId
     * @param empList
     * @return
     */
    public String getUsersId(Integer empId,List<Employee> empList){
        List<String> result=new ArrayList<>();
        String userId="";
        String usersId="";
        for(Employee employee:empList){
            if(employee.getManager()!=null||employee.getManager()!=0){
                if(employee.getManager().equals(empId)){
                    usersId+=employee.getId()+",";
                    result.add(employee.getId()+"");
                }
            }
        }
        for(String str:result){
            String userId1=getUsersId(Integer.parseInt(str),empList);
            if(userId1!=null&&!userId1.equals("")){
                userId+=userId1;
            }
        }
        if(userId!=null&&!userId.equals("null")){
            usersId+=userId;
        }
        return usersId;
    }

    /**
     * 获取绩效数据
     * @param request
     * @return
     */
    @RequestMapping("/findPeAcc")
    @ResponseBody
    public String findPeAcc(HttpServletRequest request){
        String employeeId = request.getParameter("employeeId");
        String cycle = request.getParameter("cycle");
        Map map=new HashMap();
        map.put("employeeId",employeeId);
        if (cycle != null) {
            if (!cycle.equals("请选择")) {
                map.put("cycle",cycle);
            }
        }
        List<Assessment> list=performanceService.findAllAcc(map);
        return JSON.toJSONString(list);
    }

    /**
     * 根据id获取业绩数据
     * @param request
     * @return
     */
    @RequestMapping("/getPeAcc")
    @ResponseBody
    public String getPeAcc(HttpServletRequest request){
        String id = request.getParameter("id");
        Performance performance=performanceService.getPeAcc(id);
        return JSON.toJSONString(performance);
    }

    /**
     * 添加业绩标准
     * @param performance
     * @return
     */
    @RequestMapping(value = "/insertPeAcc",method = {RequestMethod.POST})
    @ResponseBody
    public String insertPeAcc(@RequestBody Performance performance){
        performanceService.insert(performance);
        if(performance.getId()>=0){
            return JSON.toJSONString(Type.SUCCESS);
        }
        return JSON.toJSONString(Type.CANCEL);
    }

    /**
     * 修改业绩
     * @param request
     * @return
     */
    @RequestMapping(value = "/updatePeAcc")
    @ResponseBody
    public String updatePeAcc(HttpServletRequest request){
        String id=request.getParameter("id");
        String weights=request.getParameter("weights");//权重
        String access=request.getParameter("access");//考核标准
        String workTasks=request.getParameter("workTasks");
        String detail=request.getParameter("detail");
        String score=request.getParameter("score");
        Performance performance=performanceService.getPeAcc(id);
        if(performance!=null){
            if(weights!=null&&!weights.equals("")){
                performance.setWeights(weights);
            }
            if(access!=null&&!access.equals("")){
                performance.setAccess(access);
            }
            if(workTasks!=null&&!access.equals("")){
                performance.setWorkTasks(workTasks);
            }
            if(detail!=null&&!detail.equals("")){
                performance.setDetail(detail);
            }
            if(score!=null&&!score.equals("")){
                performance.setScore(score);
            }

            int result=performanceService.update(performance);
            if(result>0){
                return JSON.toJSONString(Type.SUCCESS);
            }else {
                return JSON.toJSONString(Type.CANCEL);
            }
        }
        return JSON.toJSONString(Type.ERROR);
    }

    /**
     * 删除业绩
     * @param request
     * @return
     */
    @RequestMapping(value = "/deletePeAcc")
    @ResponseBody
    public String deletePeAcc(HttpServletRequest request){
        String id=request.getParameter("id");
        if(id!=null){
            performanceService.deletePeAccById(id);
            Performance performance=performanceService.getPeAcc(id);
            if(performance==null||performance.getId()<=0){
                return JSON.toJSONString(Type.SUCCESS);
            }
        }
        return JSON.toJSONString(Type.ERROR);
    }

    /**
     * 获取行为考核
     * @param request
     * @return
     */
    @RequestMapping("/selectBeCycle")
    @ResponseBody
    public String selectBeCycle(HttpServletRequest request){
        String employeeId=request.getParameter("employeeId");
        List<String> list=null;
        if(employeeId!=null){
            list=behaviorService.selectBeCycle(employeeId);
        }
        return JSON.toJSONString(list);
    }

    @ResponseBody
    @RequestMapping(value = "/updateBehavior",method = {RequestMethod.POST})
    public String insertBe(@RequestBody Behavior behavior) {
        try {
            behaviorService.update(behavior);
        } catch (Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(Type.ERROR);
        }
        return JSON.toJSONString(Type.SUCCESS);
    }

    /**
     * 行为表查询
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/findBehavior")
    public String findBe(HttpServletRequest request) {
        String cycle=request.getParameter("cycle");
        String employeeId=request.getParameter("employeeId");
        Behavior behavior = new Behavior();
        if(employeeId!=null){
            behavior.setEmployeeId(Integer.parseInt(employeeId));
        }
        if(cycle!=null){
            behavior.setCycle(cycle);
        }
        Behavior behaviorResult=behaviorService.findAllBe(behavior);
        List<Behavior> list=new ArrayList<>();
        list.add(behaviorResult);
        return JSON.toJSONString(list);
    }

    @ResponseBody
    @RequestMapping("/getAssessmentByEmployeeId")
    public String getAssessmentByEmployeeId(HttpServletRequest request) {
        String cycle=request.getParameter("cycle");
        String employeeId=request.getParameter("employeeId");
        Map map=new HashMap();
        if(cycle!=null&&!cycle.equals("")){
            map.put("cycle",cycle);
        }
        if(employeeId!=null&&!employeeId.equals("")){
            map.put("employeeId",employeeId);
        }
        Assessment assessment=behaviorService.getAssessmentByEmployeeId(map);
        return JSON.toJSONString(assessment);
    }

    @RequestMapping(value = "/copyPeAccMonth",method = {RequestMethod.POST})
    @ResponseBody
    public String copyPeAccMonth(@RequestBody String Data){
         if(Data!=null&&!Data.equals("")){

        }
        return JSON.toJSONString("");
    }
}
