package com.howei.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import java.math.BigDecimal;
import java.util.*;

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

    @RequestMapping("/getUserInform")
    @ResponseBody
    public Map getUserInform(){
        Subject subject=SecurityUtils.getSubject();
        Users users=(Users) subject.getPrincipal();
        Map map=new HashMap();
        if (users!=null){
            map.put("userName",users.getUserName());
            map.put("userNumber",users.getUserNumber());
        }
        return map;
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
        if(list!=null){
            for(int i=0;i<list.size();i++){
                Assessment assessment=list.get(i);
                //计算综合绩效与净绩效
                double score1=assessment.getScore1();
                double score2=assessment.getScore2();
                double jianban=assessment.getJiaban();
                //净绩效=(行为* 0.5 + 业绩 * 0.5)/90
                BigDecimal bd = new BigDecimal((score1*0.5+score2*0.5)/90);
                double netPerformance=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                assessment.setNetPerformance(netPerformance);
                //综合绩效=净绩效+加班*0.01
                bd = new BigDecimal(netPerformance+jianban*0.01);
                double comprehensivePerformance=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                assessment.setComprehensivePerformance(comprehensivePerformance);
                if(users==null){

                }else{
                    if(assessment.getUserNumber().equals(users.getUserNumber())){
                        list.remove(i);
                        list.add(0,assessment);
                    }
                }
            }
        }
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
     * id：id
     * 工作任务：workTasks
     * 考核标准：access
     * 考核详情：detail
     * 考核分：score
     * 权重： weights
     * 修改业绩
     * @return
     */
    @RequestMapping(value = "/updatePeAcc")
    @ResponseBody
    public String updatePeAcc(@RequestBody String obj){
        if(obj!=null){
            List list=(List)JSONArray.parse(obj);
            try {
                for (int i=0;i<list.size();i++){
                    Map<String,Object> map=(Map<String, Object>) list.get(i);
                    String id=map.get("id")+"";
                    String weights=map.get("weights")+"";//权重
                    String access=map.get("access")+"";//考核标准
                    String workTasks=map.get("workTasks")+"";
                    String detail=map.get("detail")+"";
                    String score=map.get("score")+"";
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
                        performanceService.update(performance);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return JSON.toJSONString(Type.ERROR);
            }
            return JSON.toJSONString(Type.SUCCESS);
        }
        return JSON.toJSONString(Type.CANCEL);
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
     * 行为表查询：获取考试成绩
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

    /**
     * 获取净绩效与综合绩效
     * @param request
     * @return
     */
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
        if(assessment!=null){
            double score1=assessment.getScore1();
            double score2=assessment.getScore2();
            double jianban=assessment.getJiaban();
            //净绩效=(行为* 0.5 + 业绩 * 0.5)/90
            BigDecimal bd = new BigDecimal((score1*0.5+score2*0.5)/90);
            double netPerformance=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            assessment.setNetPerformance(netPerformance);
            //综合绩效=净绩效+加班*0.01
            bd = new BigDecimal(netPerformance+jianban*0.01);
            double comprehensivePerformance=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            assessment.setComprehensivePerformance(comprehensivePerformance);
        }
        return JSON.toJSONString(assessment);
    }

    /**
     * 根据加班计算综合绩效与净绩效
     * @return
     */
    @ResponseBody
    @RequestMapping("/getAssessmentByJiaban")
    public String getAssessmentByJiaban(HttpServletRequest request){
        String cycle=request.getParameter("cycle");
        String employeeId=request.getParameter("employeeId");
        String jiaban=request.getParameter("jiaban");
        double jiabanDouble=0.00;
        if(jiaban!=null&&!jiaban.equals("")){
            jiabanDouble=Double.parseDouble(jiaban);
        }
        Map map=new HashMap();
        if(cycle!=null&&!cycle.equals("")){
            map.put("cycle",cycle);
        }
        if(employeeId!=null&&!employeeId.equals("")){
            map.put("employeeId",employeeId);
        }
        Assessment assessment=behaviorService.getAssessmentByEmployeeId(map);
        if(assessment!=null){
            map.clear();
            double score1=assessment.getScore1();
            double score2=assessment.getScore2();
            //净绩效=(行为* 0.5 + 业绩 * 0.5)/90
            BigDecimal bd = new BigDecimal((score1*0.5+score2*0.5)/90);
            double netPerformance=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            //综合绩效=净绩效+加班*0.01
            bd = new BigDecimal(netPerformance+jiabanDouble*0.01);
            double comprehensivePerformance=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            map.put("comprehensivePerformance",comprehensivePerformance);
            map.put("netPerformance",netPerformance);
        }
        return JSON.toJSONString(map);
    }

    @RequestMapping(value = "/copyPeAccMonth",method = {RequestMethod.POST})
    @ResponseBody
    public String copyPeAccMonth(@RequestBody String Data){
         if(Data!=null&&!Data.equals("")){

        }
        return JSON.toJSONString("");
    }

    /**
     * 复制考核周期
     * @param employeeId
     * @param cycle
     * @param lastcycle
     * @return
     */
    @ResponseBody
    @RequestMapping("/copyPeAcc")
    public String copyPeAcc(Integer employeeId, String cycle, String lastcycle) {
        //判斷複製周期是否存在10條記錄
        Performance performance = new Performance();
        performance.setemployeeId(employeeId);
        performance.setCycle(lastcycle);
        List<Performance> p=performanceService.findPeAcc(performance);
        if(p==null||p.size()==0){
            performance = new Performance();
            performance.setemployeeId(employeeId);
            performance.setCycle(cycle);
            p = performanceService.findPeAcc(performance);
            for (int i = 0; i < p.size(); i++) {
                Performance performance1 = p.get(i);
                performance1.setCycle(lastcycle);
                performanceService.insert(performance1);
            }
            return "success";
        }
        return "havaRecord";//存在記錄
    }
}
