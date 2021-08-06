package com.howei.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.howei.pojo.*;
import com.howei.service.BehaviorService;
import com.howei.service.EmployeeService;
import com.howei.service.PerformanceService;
import com.howei.service.WorkingService;
import com.howei.util.*;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.ResultType;
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
import java.util.stream.Collectors;

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

    @RequestMapping("/getUserInfo")
    @ResponseBody
    public Result getUserInform() {
        Subject subject = SecurityUtils.getSubject();
        Users users = (Users) subject.getPrincipal();
        if (users == null) {
            return Result.fail(ResultEnum.NO_USER);
        }
        Map map = new HashMap(2);
        map.put("userName", users.getUserName());
        map.put("userNumber", users.getUserNumber());
        return Result.ok(1, map);
    }

    /**
     * 查询所有员工业绩
     *
     * @param request
     * @return
     */
    @RequestMapping("/getAssessment")
    @ResponseBody
    public Result getAssessment(HttpServletRequest request) {
        String cycle = request.getParameter("cycle");
        String empIdStr = "";
        Users users = this.getPrincipal();
        if (users == null) {
            return Result.fail(ResultEnum.NO_USER);
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
        if (list == null || list.size() == 0) {
            return Result.ok(0, new ArrayList<>());
        }
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
            //将登录用户放在首位
            if (users.getUserNumber().equals(assessment.getUserNumber())) {
                list.remove(i);
                list.add(0, assessment);
            }
        }
        return Result.ok(list.size(), list);
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
        String searchWord = request.getParameter("searchWord");
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        Map map = new HashMap();
        map.put("employeeId", employeeId);
        if (cycle != null) {
            map.put("cycle", cycle);
        }
        if (isActive != null) {
            map.put("isActive", isActive);
        }
        if (!StringUtils.isEmpty(searchWord)) {
            map.put("searchWord", searchWord);
        }
        List<Performance> list = performanceService.findAllAcc(map);
        if (list == null || list.size() == 0) {
            return Result.ok(0, new ArrayList<>());
        }
        int count = list.size();
        if (page != null && limit != null) {
            list = list.stream().skip(Integer.parseInt(page) - 1).limit(Integer.parseInt(limit)).collect(Collectors.toList());
        }
        return Result.ok(count, list);
    }

//    /**
//     * 根据id获取业绩数据
//     *
//     * @param request
//     * @return
//     */
//    @RequestMapping("/getPeAcc")
//    @ResponseBody
//    public String getPeAcc(HttpServletRequest request) {
//        String id = request.getParameter("id");
//        Performance performance = performanceService.getPeAcc(id);
//        return JSON.toJSONString(performance);
//    }

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
            return Result.fail(ResultEnum.NO_USER);
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
            return Result.fail(ResultEnum.NO_USER);
        }
        performanceService.updateIsActiveByIds(ids, isActive);

        return Result.ok();
    }


    /**
     * 修改业绩
     *
     * @return
     */
    @RequestMapping(value = "/updatePeAcc")
    @ResponseBody
    public Result updatePeAcc(@RequestBody String obj) {
        Subject subject = SecurityUtils.getSubject();
        Users users = (Users) subject.getPrincipal();
        if (users == null) {
            return Result.fail(ResultEnum.NO_USER);
        }
        if (obj == null || "".equals(obj)) {
            return Result.fail(ResultEnum.NO_PARAMETERS);
        }
        List list = (List) JSONArray.parse(obj);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = (Map<String, Object>) list.get(i);
            String id = map.get("id").toString();
            String weights = map.get("weights").toString();//权重
            String access = map.get("access").toString();//考核标准
            String workTasks = map.get("workTasks").toString();
            String detail = map.get("detail").toString();
            String score = map.get("score").toString();
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
        return Result.ok();
    }

    /**
     * 删除业绩
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/deletePeAcc")
    @ResponseBody
    public Result deletePeAcc(HttpServletRequest request) {
        String id = request.getParameter("id");
        if (StringUtils.isEmpty(id)) {
            return Result.fail(ResultEnum.NO_PARAMETERS);
        }
        performanceService.deletePeAccById(id);
        return Result.ok();
    }

//    /**
//     * 获取行为考核
//     *
//     * @param request
//     * @return
//     */
//    @RequestMapping("/selectBeCycle")
//    @ResponseBody
//    public String selectBeCycle(HttpServletRequest request) {
//        String employeeId = request.getParameter("employeeId");
//        List<String> list = null;
//        if (employeeId != null) {
//            list = behaviorService.selectBeCycle(employeeId);
//        }
//        return JSON.toJSONString(list);
//    }

    @ResponseBody
    @RequestMapping(value = "/updateBehavior", method = {RequestMethod.POST})
    public Result insertBe(@RequestBody Behavior behavior) {
        Integer id = behavior.getId();
        if (StringUtils.isEmpty(id) || id == 0) {
            behaviorService.insert(behavior);
        } else {
            behaviorService.update(behavior);
        }
        return Result.ok();
    }

    /**
     * 行为表查询：获取考试成绩
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/findBehavior")
    public Result findBe(HttpServletRequest request) {
        String cycle = request.getParameter("cycle");
        String employeeId = request.getParameter("employeeId");
        Behavior behaviorQO = new Behavior();
        if (employeeId != null) {
            behaviorQO.setEmployeeId(Integer.parseInt(employeeId));
        }
        if (cycle != null) {
            behaviorQO.setCycle(cycle);
        }
        Behavior behavior = behaviorService.findAllBe(behaviorQO);
        return Result.ok(1, behavior);
    }

    /**
     * 获取净绩效与综合绩效
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getAssessmentByEmployeeId")
    public Result getAssessmentByEmployeeId(HttpServletRequest request) {
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
        return Result.ok(1, assessment);
    }

    /**
     * 根据加班计算综合绩效与净绩效
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/getAssessmentByJiaban")
    public Result getAssessmentByJiaban(HttpServletRequest request) {
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
        map.clear();
        if (assessment != null) {
            double score1 = assessment.getScore1();
            double score2 = assessment.getScore2();
            //净绩效=(行为* 0.5 + 业绩 * 0.5)/90
            BigDecimal bd = new BigDecimal((score1 * 0.5 + score2 * 0.5) / 90);
            double netPerformance = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            //综合绩效=净绩效
            bd = new BigDecimal(netPerformance);
            double comprehensivePerformance = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            map.put("comprehensivePerformance", comprehensivePerformance);
            map.put("netPerformance", netPerformance);
        }
        return Result.ok(1, map);
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
    public Result copyPeAcc(Integer employeeId, String cycle, String lastcycle) {
        //判斷複製周期是否存在10條記錄
        Performance performance = new Performance();
        performance.setEmployeeId(employeeId);
        performance.setCycle(lastcycle);
        List<Performance> performanceList = performanceService.findPeAcc(performance);
        if (performanceList != null && performanceList.size() > 0) {
            return Result.fail(ResultEnum.HAVE_RECORD);
        }
        performance = new Performance();
        performance.setEmployeeId(employeeId);
        performance.setCycle(cycle);
        performanceList = performanceService.findPeAcc(performance);
        for (int i = 0; i < performanceList.size(); i++) {
            Performance performance1 = performanceList.get(i);
            performance1.setCycle(lastcycle);
            performanceService.insert(performance1);
        }
        return Result.ok();
    }
}
