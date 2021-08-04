package com.howei.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.howei.pojo.*;
import com.howei.service.BehaviorService;
import com.howei.service.EmployeeService;
import com.howei.service.PerformanceService;
import com.howei.service.WorkingService;
import com.howei.util.*;
import org.apache.catalina.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
public class AchievementsController {

    @Autowired
    private BehaviorService behaviorService;

    @Autowired
    private PerformanceService performanceService;

    @Autowired
    private EmployeeService employeeService;


    @Autowired
    private WorkingService workingService;

    @RequestMapping("/toAchievements")
    public String toAchievements() {
        return "achievements";
    }

    public Users getPrincipal() {
        Subject subject = SecurityUtils.getSubject();
        Users users = (Users) subject.getPrincipal();
        return users;
    }

    @RequestMapping("/getUserInform")
    @ResponseBody
    public Map getUserInform() {
        Subject subject = SecurityUtils.getSubject();
        Users users = (Users) subject.getPrincipal();
        Map map = new HashMap();
        if (users != null) {
            map.put("userName", users.getUserName());
            map.put("userNumber", users.getUserNumber());
        }
        return map;
    }

    /**
     * 查询所有员工业绩
     *
     * @param request
     * @return
     */
    @RequiresPermissions(value = {"绩效查询"}, logical = OR)
    @RequestMapping("/getAssessment")
    @ResponseBody
    public String getAssessment(HttpServletRequest request) {
        String cycle = request.getParameter("cycle");
        String empIdStr = "";
        Users users = this.getPrincipal();
        if (users == null) {
            return "noUser";
        }
        empIdStr += users.getEmployeeId() + ",";
        List<String> employeeIdList = new ArrayList<>();

        List<Employee> rootList = employeeService.getEmployeeByManager(users.getEmployeeId());

        List<Employee> empList = employeeService.getEmployeeByManager(0);
        ListUtils.getChildEmployeeId(rootList, empList, employeeIdList, null);

        for (String employeeIdStr : employeeIdList) {
            empIdStr += employeeIdStr + ",";
        }
        if (empIdStr != null && !empIdStr.equals("")) {
            empIdStr = empIdStr.substring(0, empIdStr.lastIndexOf(","));
        }
        if (cycle == null || cycle.equals("")) {
            cycle = DateFormat.beforeMonth();
        }
        Map map = new HashMap();
        map.put("cycle", cycle);
        map.put("empId", empIdStr);
        List<Assessment> list = behaviorService.getAssessment(map);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Assessment assessment = list.get(i);
                //计算综合绩效与净绩效
                double score1 = assessment.getScore1();
                double score2 = assessment.getScore2();
                //净绩效=(行为* 0.5 + 业绩 * 0.5)/90
                BigDecimal bd = new BigDecimal((score1 * 0.5 + score2 * 0.5) / 90);
                double netPerformance = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                assessment.setNetPerformance(netPerformance);
                //综合绩效=净绩效
                assessment.setComprehensivePerformance(netPerformance);
                if (users == null) {

                } else {
                    if (assessment.getUserNumber().equals(users.getUserNumber())) {
                        list.remove(i);
                        list.add(0, assessment);
                    }
                }
            }
        }
        Result result = new Result();
        result.setData(list);
        result.setCount(list.size());
        result.setCode(0);
        result.setMsg("success");
        return JSON.toJSONString(result);
    }


    /**
     * 获取绩效数据
     *
     * @param request
     * @return
     */
    @RequestMapping("/findPeAcc")
    @ResponseBody
    public Result findPeAcc(HttpServletRequest request) {

        String employeeId = request.getParameter("employeeId");
        String cycle = request.getParameter("cycle");
        String isActive = request.getParameter("isActive");
        Map map = new HashMap();
        map.put("employeeId", employeeId);
        if (cycle != null) {
            map.put("cycle", cycle);
        }
        if (isActive != null) {
            map.put("isActive", isActive);
        }
        List<Performance> list = performanceService.findAllAcc(map);

        return Result.ok(list.size(), list);
    }

    /**
     * 根据id获取业绩数据
     *
     * @param request
     * @return
     */
    @RequestMapping("/getPeAcc")
    @ResponseBody
    public String getPeAcc(HttpServletRequest request) {
        String id = request.getParameter("id");
        Performance performance = performanceService.getPeAcc(id);
        return JSON.toJSONString(performance);
    }

    /**
     * 添加业绩标准
     *
     * @param performance
     * @return
     */
    @RequestMapping(value = "/insertPeAcc", method = {RequestMethod.POST})
    @ResponseBody
    public Result insertPeAcc(@RequestBody Performance performance) {
        Subject subject = SecurityUtils.getSubject();
        Users user = (Users) subject.getPrincipal();
        if (user == null) {
            Result.fail(Type.noUser);
        }
        performanceService.insert(performance);
        return Result.ok();
    }

    /**
     * 批量修改,修改isActive字段
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/batchUpdatePeAcc", method = {RequestMethod.POST})
    @ResponseBody
    public Result batchInsertPeAcc(@RequestParam String ids, @RequestParam boolean isActive) {
        Subject subject = SecurityUtils.getSubject();
        Users user = (Users) subject.getPrincipal();
        if (user == null) {
            Result.fail(Type.noUser);
        }
        int count = performanceService.updateIsActiveByIds(ids,isActive);


        return Result.ok();
    }


    /**
     * 修改业绩
     *
     * @return
     */
    @RequestMapping(value = "/updatePeAcc")
    @ResponseBody

    public String updatePeAcc(@RequestBody String obj) {
        if (obj != null) {
            List list = (List) JSONArray.parse(obj);
            try {
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = (Map<String, Object>) list.get(i);
                    String id = map.get("id") + "";
                    String weights = map.get("weights") + "";//权重
                    String access = map.get("access") + "";//考核标准
                    String workTasks = map.get("workTasks") + "";
                    String detail = map.get("detail") + "";
                    String score = map.get("score") + "";
                    Performance performance = performanceService.getPeAcc(id);
                    if (performance != null) {
                        if (weights != null && !weights.equals("")) {
                            performance.setWeights(weights);
                        }
                        if (access != null && !access.equals("")) {
                            performance.setAccess(access);
                        }
                        if (workTasks != null && !access.equals("")) {
                            performance.setWorkTasks(workTasks);
                        }
                        if (detail != null && !detail.equals("")) {
                            performance.setDetail(detail);
                        }
                        if (score != null && !score.equals("")) {
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
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/deletePeAcc")
    @ResponseBody
    public String deletePeAcc(HttpServletRequest request) {
        String id = request.getParameter("id");
        if (StringUtils.isEmpty(id)) {
            return JSON.toJSONString(Type.ERROR);
        }
        performanceService.deletePeAccById(id);
        Performance performance = performanceService.getPeAcc(id);
        return JSON.toJSONString(Type.SUCCESS);
    }

    /**
     * 获取行为考核
     *
     * @param request
     * @return
     */
    @RequestMapping("/selectBeCycle")
    @ResponseBody
    public String selectBeCycle(HttpServletRequest request) {
        String employeeId = request.getParameter("employeeId");
        List<String> list = null;
        if (employeeId != null) {
            list = behaviorService.selectBeCycle(employeeId);
        }
        return JSON.toJSONString(list);
    }

    @ResponseBody
    @RequestMapping(value = "/updateBehavior", method = {RequestMethod.POST})
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
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/findBehavior")
    public String findBe(HttpServletRequest request) {
        String cycle = request.getParameter("cycle");
        String employeeId = request.getParameter("employeeId");
        Behavior behavior = new Behavior();
        if (employeeId != null) {
            behavior.setEmployeeId(Integer.parseInt(employeeId));
        }
        if (cycle != null) {
            behavior.setCycle(cycle);
        }
        Behavior behaviorResult = behaviorService.findAllBe(behavior);
        List<Behavior> list = new ArrayList<>();
        list.add(behaviorResult);
        return JSON.toJSONString(list);
    }

    /**
     * 获取净绩效与综合绩效
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getAssessmentByEmployeeId")
    public String getAssessmentByEmployeeId(HttpServletRequest request) {
        String cycle = request.getParameter("cycle");
        String employeeId = request.getParameter("employeeId");
        Map map = new HashMap();
        if (cycle != null && !cycle.equals("")) {
            map.put("cycle", cycle);
        }
        if (employeeId != null && !employeeId.equals("")) {
            map.put("employeeId", employeeId);
        }

        Assessment assessment = behaviorService.getAssessmentByEmployeeId(map);
        map.clear();
        map.put("monthDay", cycle + "-01");
        map.put("empIdStr", employeeId);
        List<OperatingHours> list = workingService.getOperatingHoursList(map);
        int size = 0;
        if (list != null && list.size() > 0) {
            size = list.size();
        }
        //获取此月天数
        int day = DateFormat.getDaysOfMonth(cycle + "-01");
        //获取此月（1-31天）数据
        int workAttendance = 0;//考勤天数
        double workingTotal = 0.0;//此月总共工时
        for (int i = 0; i < day; i++) {
            if (i < size) {
                OperatingHours operatingHours = list.get(i);
                double workingTime = operatingHours.getWorkingTime();//工时
                //考勤天数:工时不为o
                if (workingTime > 0) {
                    workAttendance++;
                }
                //此月总共工时
                workingTotal += workingTime;
            }
        }
        //本月要求工时数
        BigDecimal bd = new BigDecimal(40 / 7.0 * day);
        double thisMonthRequirementTime = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//本月工时
        //判断是否有加班工时
        double workOvertime = 0.0;
        if (workingTotal > thisMonthRequirementTime) {
            bd = new BigDecimal(workingTotal - thisMonthRequirementTime);
            workOvertime = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//加班工时
        } else {
            workOvertime = 0.0;//加班工时
        }
        assessment.setJiaban(workOvertime);
        assessment.setKaoqin(workAttendance);
        if (assessment != null) {
            double score1 = assessment.getScore1();
            double score2 = assessment.getScore2();
            //净绩效=(行为* 0.5 + 业绩 * 0.5)/90
            bd = new BigDecimal((score1 * 0.5 + score2 * 0.5) / 90);
            double netPerformance = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            assessment.setNetPerformance(netPerformance);
            //综合绩效=净绩效
            assessment.setComprehensivePerformance(netPerformance);
        }
        return JSON.toJSONString(assessment);
    }

    /**
     * 根据加班计算综合绩效与净绩效
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/getAssessmentByJiaban")
    public String getAssessmentByJiaban(HttpServletRequest request) {
        String cycle = request.getParameter("cycle");
        String employeeId = request.getParameter("employeeId");
        String jiaban = request.getParameter("jiaban");
        double jiabanDouble = 0.00;
        if (jiaban != null && !jiaban.equals("")) {
            jiabanDouble = Double.parseDouble(jiaban);
        }
        Map map = new HashMap();
        if (cycle != null && !cycle.equals("")) {
            map.put("cycle", cycle);
        }
        if (employeeId != null && !employeeId.equals("")) {
            map.put("employeeId", employeeId);
        }
        Assessment assessment = behaviorService.getAssessmentByEmployeeId(map);
        if (assessment != null) {
            map.clear();
            double score1 = assessment.getScore1();
            double score2 = assessment.getScore2();
            //净绩效=(行为* 0.5 + 业绩 * 0.5)/90
            BigDecimal bd = new BigDecimal((score1 * 0.5 + score2 * 0.5) / 90);
            double netPerformance = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            //综合绩效=净绩效+加班*0.01
            bd = new BigDecimal(netPerformance + jiabanDouble * 0.01);
            double comprehensivePerformance = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            map.put("comprehensivePerformance", comprehensivePerformance);
            map.put("netPerformance", netPerformance);
        }
        return JSON.toJSONString(map);
    }

    @RequestMapping(value = "/copyPeAccMonth", method = {RequestMethod.POST})
    @ResponseBody
    public String copyPeAccMonth(@RequestBody String Data) {
        if (Data != null && !Data.equals("")) {

        }
        return JSON.toJSONString("");
    }

    /**
     * 复制考核周期
     *
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
        List<Performance> p = performanceService.findPeAcc(performance);
        if (p == null || p.size() == 0) {
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
