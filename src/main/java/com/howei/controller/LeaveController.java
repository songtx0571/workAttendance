package com.howei.controller;

import com.alibaba.fastjson.JSON;
import com.howei.pojo.*;
import com.howei.service.EmployeeService;
import com.howei.service.LeaveService;
import com.howei.service.UserService;
import com.howei.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.shiro.authz.annotation.Logical.OR;

/**
 * 请假考勤
 */
@Controller
@RequestMapping("/wa/leave")
@CrossOrigin
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;


    /**
     * 跳转请假配置页面
     *
     * @return
     */
    @RequestMapping("/toLeaveConfigure")
    public String toLeaveConfigure() {

        return "leaveConfigure";
    }

    /**
     * 跳转请假统计页面
     *
     * @return
     */
    @RequestMapping("/toLeaveStatistics")
    public String toLeaveStatistics() {
        return "leaveStatistics";
    }

    /**
     * 跳转请假页面
     *
     * @return
     */
    @RequestMapping("/toLeave")
    public String toLeave() {
        return "leave";
    }

    public Users getPrincipal() {
        Subject subject = SecurityUtils.getSubject();
        Users users = (Users) subject.getPrincipal();
        return users;
    }

    /*****************************************************请假配置********************************************************/

    /**
     * 获取配置列表
     *
     * @param request
     * @return
     */
    @RequiresPermissions(value = {"请假配置"})
    @RequestMapping("/getConfigureList")
    @ResponseBody
    public Result getConfigureList(HttpServletRequest request) {
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        List<Leave> list = leaveService.getConfigureList(null);
        int count = list.size();
        if (!StringUtils.isEmpty(page) && !StringUtils.isEmpty(limit)) {
            int pageNum = Integer.parseInt(page);
            int limitNum = Integer.parseInt(limit);
            list = list.stream().skip((pageNum - 1) * limitNum).limit(limitNum).collect(Collectors.toList());
        }
        return Result.ok(count, list);
    }

    /**
     * 添加配置
     *
     * @param leave
     * @return
     */
    @RequiresPermissions(value = {"请假配置"}, logical = OR)
    @RequestMapping(value = "/addConfigure", method = {RequestMethod.POST})
    @ResponseBody
    public Result addConfigure(@RequestBody Leave leave) {
        leave.setStatus(0);
        leave.setCreated(DateFormat.getYMDHMS(new Date()));
        int result = leaveService.addConfigure(leave);
        if (result >= 0) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 修改配置
     *
     * @param request
     * @return
     */
    @RequiresPermissions(value = {"请假配置"}, logical = OR)
    @RequestMapping("/updateConfigure")
    @ResponseBody
    public Result updateConfigure(HttpServletRequest request) {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String data = request.getParameter("data");
        String status = request.getParameter("status");
        String unit = request.getParameter("unit");
        String remark = request.getParameter("remark");
        String quota = request.getParameter("quota");

        Leave leave = null;
        if (!StringUtils.isEmpty(id)) {
            leave = leaveService.getLeave(id);
        }
        if (leave != null && !"".equals(leave.getCreated())) {
            if (!StringUtils.isEmpty(name)) {
                leave.setName(name);
            }
            if (!StringUtils.isEmpty(data)) {
                leave.setData(Double.valueOf(data));
            }
            if (!StringUtils.isEmpty(status)) {
                leave.setStatus(Integer.parseInt(status));
            }
            if (!StringUtils.isEmpty(unit)) {
                leave.setUnit(Integer.parseInt(unit));
            }
            if (!StringUtils.isEmpty(quota)) {
                leave.setQuota(Integer.parseInt(quota));
            }
            if (!StringUtils.isEmpty(remark)) {
                leave.setRemark(remark);
            }
            int count = leaveService.updateConfigure(leave);
            if (count >= 0) {
                return Result.ok();
            }
        }
        return Result.fail();
    }

    /*****************************************************请假查询********************************************************/

    /**
     * 获取请假数据列表
     *
     * @return
     */
    @RequiresPermissions(value = {"请假查询"}, logical = OR)
    @RequestMapping("/getLeaveDataList")
    @ResponseBody
    public Result getLeaveDataList(HttpServletRequest request) {

        Users users = this.getPrincipal();
        if (users == null) {
            return Result.fail(ResultEnum.NO_USER);
        }
        Map<Integer, String> empName = employeeService.getEmployeeMap();
        String startTime = request.getParameter("startTime");
        String empId = request.getParameter("employeeId");
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        Integer departmentId = users.getDepartmentId();//部门
        Integer employeeId = users.getEmployeeId();//请假人:请假人为空即为当前登录人
        //获取请假人信息
        List<String> employeeIdList = new ArrayList<>();
        employeeIdList.add(employeeId.toString());
        List<Employee> rootList = employeeService.getEmployeeByManager(employeeId);
        List<Employee> empList = employeeService.getEmployeeByManager(0);
        ListUtils.getChildEmployeeId(rootList, empList, employeeIdList, null);
        String empIdStr = employeeIdList.stream().collect(Collectors.joining(","));
        Map map = new HashMap();
        if (!StringUtils.isEmpty(startTime)) {
            map.put("startTime", startTime + "-01");
        } else {
            map.put("startTime", DateFormat.getYMDHM(new Date()));
        }
        if (!StringUtils.isEmpty(empId)) {
            map.put("employeeId", empId);
        } else {
            map.put("employeeId", empIdStr);
        }
        map.put("departmentId", departmentId);
        List<LeaveData> list = leaveService.getLeaveDataList(map);
        if (list == null || list.size() == 0) {
            return Result.ok(0, new ArrayList<>());
        }
        int count = list.size();
        if (!StringUtils.isEmpty(page) && !StringUtils.isEmpty(limit)) {
            int pageNum = Integer.parseInt(page);
            int limitNum = Integer.parseInt(limit);
            list = list.stream().skip((pageNum - 1) * limitNum).limit(limitNum).collect(Collectors.toList());
        }
        for (LeaveData leaveData : list) {
            String startTimeed = leaveData.getStartTime();//请假开始时间
            String endTimeed = leaveData.getEndTime();//请假结束时间
            //计算两个日期之间间隔
            String bothTime;
            try {
                bothTime = DateFormat.getBothDate(startTimeed, endTimeed);
            } catch (ParseException e) {
                bothTime = "";
            }
            leaveData.setBothTime(bothTime);
            Integer status = leaveData.getStatus();
            String review = leaveData.getReview();
            String reviewRemark = leaveData.getReviewRemark();
            String reviewTime = leaveData.getReviewTime();
            if (status == 2 && review != null) {
                int index = review.indexOf("=1");
                if (index == -1) {
                    leaveData.setReviewResult("通过");
                } else {
                    leaveData.setReviewResult("未通过");
                }
            }
            if (status == 1 || status == 2) {
                String[] reviewArr = review.split(",");
                String[] reviewRemarkArr = reviewRemark.split(",");
                String[] reviewTimeArr = reviewTime.split(",");
                String reviewed = "";
                String reviewedRemark = "";
                String reviewedTime = "";
                for (String str : reviewArr) {
                    String[] reviewArr1 = str.split("=");
                    String name = empName.get(Integer.parseInt(reviewArr1[0]));
                    if (reviewArr1[1].equals("0")) {
                        reviewed += name + "=" + "同意,";
                    } else {
                        reviewed += name + "=" + reviewArr1[1] + ",";
                    }
                }
                leaveData.setReview(reviewed);
                if (reviewRemarkArr != null && reviewRemarkArr.length > 0) {
                    for (String str : reviewRemarkArr) {
                        String[] reviewArr1 = str.split("=");
                        String name = empName.get(Integer.parseInt(reviewArr1[0]));
                        if (reviewArr1.length == 2) {
                            reviewedRemark += name + "=" + reviewArr1[1] + ",";
                        }
                    }
                }
                if (!"".equals(reviewedRemark)) {
                    leaveData.setReviewRemark(reviewedRemark);
                }
                if (reviewTimeArr != null && reviewTimeArr.length > 0) {
                    for (String str : reviewTimeArr) {
                        String[] reviewArr1 = str.split("=");
                        String name = empName.get(Integer.parseInt(reviewArr1[0]));
                        if (reviewArr1.length == 2) {
                            reviewedTime += name + "=" + reviewArr1[1] + ",";
                        }
                    }
                }
                if (!"".equals(reviewedTime)) {
                    leaveData.setReviewTime(reviewedTime);
                }
            }
        }
        return Result.ok(count, list);
    }

    /**
     * 下拉框员工
     *
     * @return
     */
    @RequiresPermissions("可选请假人")
    @RequestMapping("/getEmployeeName")
    @ResponseBody
    public Result getEmployeeName() {
        Users users = this.getPrincipal();
        if (users == null) {
            return Result.fail(ResultEnum.NO_USER);
        }
        Integer employeeId = users.getEmployeeId();
        List<String> employeeIdList = new ArrayList<>();
        employeeIdList.add(employeeId.toString());
        List<Employee> rootList = employeeService.getEmployeeByManager(employeeId);
        List<Employee> empList = employeeService.getEmployeeByManager(0);
        ListUtils.getChildEmployeeId(rootList, empList, employeeIdList, null);
        String empIdStr = employeeIdList.stream().collect(Collectors.joining(","));
        List<Map<String, String>> list = employeeService.getEmployeeNameMapByManager(empIdStr);
        return Result.ok(list.size(), list);
    }


    /**
     * 获取请假配置项信息
     *
     * @return
     */
    @RequestMapping("/getLeaveNameMap")
    @ResponseBody
    public Result getLeaveNameMap() {
        Users users = this.getPrincipal();
        if (users == null) {
            return Result.fail(ResultEnum.NO_USER);
        }
        List<Map> list = leaveService.getConfigureMap();
        return Result.ok(list.size(), list);
    }

    @RequiresPermissions(value = {"请假查询"}, logical = OR)
    @RequestMapping(value = "/addLeaveData", method = {RequestMethod.POST})
    @ResponseBody
    public Result addLeaveData(@RequestBody LeaveData leaveData) {
        Users loginUser = this.getPrincipal();
        if (loginUser == null) {
            return Result.fail(ResultEnum.NO_USER);
        }
        Integer employeeId = leaveData.getEmployeeId();//请假人
        Integer leaveId = leaveData.getLeaveId();//请假配置Id
        String startTime = leaveData.getStartTime();
        Integer quota = null;
        //判断请假人的请假是否超额
        Leave leave = leaveService.getLeave(leaveId + "");
        if (leave != null) {
            quota = leave.getQuota();//获取限额
        }
        //判断请假人是否为空
        if (employeeId == null || employeeId == 0) {
            employeeId = loginUser.getEmployeeId();
            leaveData.setEmployeeId(employeeId);
        }

        Map map = new HashMap();
        map.put("employeeId", employeeId);
        map.put("leaveId", leaveId);
        map.put("startTime", startTime);
        List<LeaveData> list = leaveService.getLeaveDataList(map);
        if (list != null && list.size() >= quota) {
            //超额：由直接绩效管理人及上级审核
            leaveData.setExcess(1);
        } else {
            leaveData.setExcess(0);
        }
        //请假人未超额：由直接绩效管理人审核
        //获取请假人的部门
        Users users = userService.getUserByEmpId(employeeId);
        if (users != null) {
            Integer departmentId = users.getDepartmentId();
            leaveData.setDepartmentId(departmentId);
        }
        leaveData.setCreatedBy(loginUser.getEmployeeId());
        //设置下一个审核人
        Employee employee = employeeService.getEmployeeById(employeeId.toString());
        if (employee != null) {
            Integer manager = employee.getManager();
            if (manager != null && !"".equals(employee.getManager())) {
                leaveData.setNextManager(manager);
            } else {
                return Result.fail(ResultEnum.TO_ADD_MANAGER);
            }
        }
        leaveData.setStatus(0);//设置请假记录状态为待审核
        leaveData.setReview("");
        leaveData.setReviewTime("");
        leaveData.setReviewRemark("");
        leaveData.setCreated(DateFormat.getYMDHMS(new Date()));
        int result = leaveService.addLeaveData(leaveData);
        if (result > 0) {
            return Result.ok();
        }
        return Result.fail();
    }

    @RequiresPermissions(value = {"请假查询"}, logical = OR)
    @RequestMapping("/updateLeaveData")
    @ResponseBody
    public Result updateLeaveData(HttpServletRequest request) {
        String id = request.getParameter("id");
        String review = request.getParameter("review");//审核0:同意;1驳回
        String reviewRemark = request.getParameter("reviewRemark");//审核意见
        Users users = this.getPrincipal();
        if (users == null) {
            return Result.fail(ResultEnum.NO_USER);
        }
        Integer employeeId = null;//请假人
        Integer manager1 = null;//上一级绩效管理人
        Integer manager = null;//当前绩效管理人
        if (StringUtils.isEmpty(id)) {
            return Result.fail(ResultEnum.NO_PARAMETERS);
        }
        LeaveData leaveData = leaveService.getLeadData(id);
        if (leaveData == null) {
            return Result.fail(ResultEnum.NO_RECORD);
        }
        manager = leaveData.getNextManager();
        //请假人
        employeeId = leaveData.getEmployeeId();
        //获取上一级绩效管理人
        Integer empId = leaveData.getNextManager();
        Employee employee1 = employeeService.getEmployeeById(empId.toString());
        if (employee1 != null) {
            manager1 = employee1.getManager();
        }
        //审核
        if (review != null) {
            String leaReview = leaveData.getReview();
            leaReview = (leaReview == null) ? "" : leaReview;
            String reviewEmpId = manager + "=" + review + ",";
            leaReview += reviewEmpId;
            leaveData.setReview(leaReview);
        }
        //审核意见
        if (reviewRemark != null) {
            String leaReviewRemark = leaveData.getReviewRemark();
            leaReviewRemark = (leaReviewRemark == null) ? "" : leaReviewRemark;
            String reviewRemarkEmpId = manager + "=" + reviewRemark + ",";
            leaReviewRemark += reviewRemarkEmpId;
            leaveData.setReviewRemark(leaReviewRemark);
        }
        //获取审核时间
        String leaReviewTime = leaveData.getReviewTime();
        leaReviewTime = (leaReviewTime == null) ? "" : leaReviewTime;
        leaReviewTime += manager + "=" + DateFormat.getYMDHM(new Date()) + ",";
        leaveData.setReviewTime(leaReviewTime);
        //修改时间
        leaveData.setUpdated(DateFormat.getYMDHMS(new Date()));
        //审核状态
        Integer quota = leaveData.getExcess();//超额数值
        //判断请假人的请假是否超额
        Integer leaveId = leaveData.getLeaveId();
        //获取月份内数据
        String startTime = leaveData.getStartTime();
        Map map = new HashMap();
        map.put("employeeId", employeeId);
        map.put("leaveId", leaveId);
        map.put("startTime", startTime);
        List<LeaveData> list = leaveService.getLeaveDataList(map);
        //判断是否超额
        if (quota == 0) {
            //未超额：修改为审核完毕
            leaveData.setStatus(2);
        } else {
            //超额数据:向上一级绩效管理人递交申请
            if (manager1 != null && !"".equals(manager1)) {
                //驳回：修改审核状态为审核完毕
                if (review.equals("1")) {
                    leaveData.setStatus(2);
                } else {
                    //同意：向上递交申请
                    leaveData.setNextManager(manager1);
                    leaveData.setStatus(1);
                }
            } else {
                //若上升到顶级审核人后，修改审核状态为审核完毕
                leaveData.setStatus(2);
            }
        }
        int index = leaveService.updateLeaveData(leaveData);
        if (index > 0) {
            //保存审核记录
            Review reviewObject = new Review();
            reviewObject.setLeaveDataId(Integer.parseInt(id));
            reviewObject.setReviewer(manager);
            reviewObject.setLeaveEmployeeId(employeeId);
            reviewObject.setResult(Integer.parseInt(review));
            leaveService.addReview(reviewObject);
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 根据条件查询请假信息列表
     *
     * @param request
     * @return
     */
    @RequiresPermissions(value = {"请假查询"}, logical = OR)
    @RequestMapping("/searchLeaveDataList")
    @ResponseBody
    public Result searchLeaveDataList(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        Users user = (Users) subject.getPrincipal();
        if (user == null) {
            return Result.fail(ResultEnum.NO_USER);
        }
        String startTime = request.getParameter("startTime");
        String employeeId = request.getParameter("employeeId");
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        Integer departmentId = user.getDepartmentId();

        Map map = new HashMap();
        if ((startTime != null)) {
            map.put("startTime", startTime + "-01");
        }
        if (departmentId != null) {
            map.put("departmentId", departmentId);
        }
        if (employeeId != null) {
            map.put("employeeId", employeeId);
        }
        List<LeaveData> list = leaveService.getLeaveDataList(map);
        int count = list.size();
        if (!StringUtils.isEmpty(page) && !StringUtils.isEmpty(limit)) {
            int pageNum = Integer.parseInt(page);
            int limitNum = Integer.parseInt(limit);
            list = list.stream().skip((pageNum - 1) * limitNum).limit(limitNum).collect(Collectors.toList());
        }
        return Result.ok(count, list);
    }

    /*****************************************************请假统计********************************************************/

    /**
     * 获取请假统计
     *
     * @return
     */
    @RequiresPermissions(value = {"请假统计"}, logical = OR)
    @RequestMapping("/getLeaveDataStatisticsList")
    @ResponseBody
    public Result getLeaveDataStatisticsList(HttpServletRequest request) {
        String month = request.getParameter("month");
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");

        Map map = new HashMap();

//        if(empId!=null&&!empId.equals("")){
//            map.put("employeeId",empId);
//        }else{
//            if(users!=null){
//                empId=users.getEmployeeId()+"";
//                if(users!=null){
//                    map.put("employeeId",empId);
//                }
//            }
//        }
//        if(companyId!=null&&!companyId.equals("")){
//            map.put("companyId",companyId);
//        }else{
//            if(users!=null){
//                map.put("companyId",users.getCompanyId());
//            }
//        }
        if (month != null && !"".equals(month)) {
            map.put("month", month + "-01");
        } else {
            month = DateFormat.getYMD();
            map.put("month", month);
        }
        Subject subject = SecurityUtils.getSubject();
        Users user = (Users) subject.getPrincipal();
        if (user == null) {
            return Result.fail(ResultEnum.NO_USER);
        }
        boolean selectAllFlag = subject.isPermitted("员工信息查询所有");
        Integer employeeId = user.getEmployeeId();
        if (selectAllFlag) {
            employeeId = 0;
        }
        String empIdStr = "";
        List<String> employeeIdList = new ArrayList<>();
        employeeIdList.add(employeeId.toString());
        List<Employee> rootList = employeeService.getEmployeeByManager(employeeId);

        List<Employee> empList = employeeService.getEmployeeByManager(0);
        ListUtils.getChildEmployeeId(rootList, empList, employeeIdList, null);
        empIdStr = employeeIdList.stream().collect(Collectors.joining(","));
        map.put("empIdStr", empIdStr);
        List<LeaveData> list = leaveService.getLeaveDataStatisticsList(map);
        int count = list.size();
        if (!StringUtils.isEmpty(page) && !StringUtils.isEmpty(limit)) {
            int pageNum = Integer.parseInt(page);
            int limitNum = Integer.parseInt(limit);
            list = list.stream().skip((pageNum - 1) * limitNum).limit(limitNum).collect(Collectors.toList());
        }
        return Result.ok(count, list);
    }

    @GetMapping("/delete")
    @ResponseBody
    public Result deleteLeave(@RequestParam(required = false) Integer id) {

        Subject subject = SecurityUtils.getSubject();
        Users user = (Users) subject.getPrincipal();
        if (user == null) {
            return Result.fail(ResultEnum.NO_USER);
        }
        leaveService.deleteById(id);
        return Result.ok();
    }

}