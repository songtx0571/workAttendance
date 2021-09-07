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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.apache.shiro.authz.annotation.Logical.OR;

/**
 * 请假考勤
 */
@Controller
@RequestMapping("/wa/leave")
@CrossOrigin(origins = "http://test.hopeop.com:80", allowCredentials = "true")
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
        Subject subject = SecurityUtils.getSubject();
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
    public String getConfigureList(HttpServletRequest request) {
        String page = request.getParameter("page");
        String pageSize = request.getParameter("limit");
        int rows = Page.getOffSet(page, pageSize);
        Map map = new HashMap();
        List<Leave> total = leaveService.getConfigureList(map);
        map.put("page", rows);
        map.put("pageSize", pageSize);
        List<Leave> list = leaveService.getConfigureList(map);
        Result result = new Result();
        result.setCode(0);
        result.setCount(total.size());
        result.setData(list);
        return JSON.toJSONString(result);
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
    public String addConfigure(@RequestBody Leave leave) {
        leave.setStatus(0);
        leave.setCreated(DateFormat.getYMDHMS(new Date()));
        int result = leaveService.addConfigure(leave);
        if (result >= 0) {
            return JSON.toJSONString(Type.SUCCESS);
        }
        return JSON.toJSONString(Type.ERROR);
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
    public String updateConfigure(HttpServletRequest request) {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String data = request.getParameter("data");
        String status = request.getParameter("status");
        String unit = request.getParameter("unit");
        String remark = request.getParameter("remark");
        String quota = request.getParameter("quota");

        Leave leave = new Leave();
        if (id != null && !id.equals("")) {
            leave = leaveService.getLeave(id);
        }
        if (leave != null && !leave.getCreated().equals("")) {
            if (name != null && !name.equals("")) {
                leave.setName(name);
            }
            if (data != null && !data.equals("")) {
                leave.setData(Double.valueOf(data));
            }
            if (status != null && !status.equals("")) {
                leave.setStatus(Integer.parseInt(status));
            }
            if (unit != null && !unit.equals("")) {
                leave.setUnit(Integer.parseInt(unit));
            }
            if (quota != null && !quota.equals("")) {
                leave.setQuota(Integer.parseInt(quota));
            }
            if (remark != null && !remark.equals("")) {
                leave.setRemark(remark);
            }
            int result = leaveService.updateConfigure(leave);
            if (result >= 0) {
                return JSON.toJSONString(Type.SUCCESS);
            }
        }
        return JSON.toJSONString(Type.ERROR);
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
    public String getLeaveDataList(HttpServletRequest request) {
         Map<Integer, String>  empName = employeeService.getEmployeeMap();
        String startTime = request.getParameter("startTime");
        String empId = request.getParameter("employeeId");
        String page = request.getParameter("page");
        String pageSize = request.getParameter("limit");
        Users users = this.getPrincipal();
        Integer departmentId = null;//部门
        Integer employeeId = null;//请假人:请假人为空即为当前登录人
        String empIdStr = "";//拼接请假人字符串
        Integer empManager = null;//请假人的绩效管理人
        if (users != null) {
            departmentId = users.getDepartmentId();
            employeeId = users.getEmployeeId();
        }
        int rows = Page.getOffSet(page, pageSize);
        //获取请假人信息
        List<String > employeeIdList=new ArrayList<>();

        List<Employee> rootList = employeeService.getEmployeeByManager(employeeId);

        List<Employee> empList = employeeService.getEmployeeByManager(0);
        ListUtils.getChildEmployeeId(rootList,empList,employeeIdList,null);

        for (String employeeIdStr : employeeIdList) {
            empIdStr+=employeeIdStr+",";
        }
        Map map = new HashMap();
        Result result = null;
        try {
            if ((startTime != null && !startTime.equals(""))) {
                map.put("startTime", startTime + "-01");
            } else {
                map.put("startTime", DateFormat.getYMDHM(new Date()));
            }
            if (empId != null && !empId.equals("")) {
                map.put("employeeId", empId);
            } else {
                map.put("employeeId", empIdStr);
            }
            map.put("departmentId", departmentId);
            List<LeaveData> total = leaveService.getLeaveDataList(map);
            map.put("page", rows);
            map.put("pageSize", pageSize);
            List<LeaveData> list = leaveService.getLeaveDataList(map);
            if (list != null && list.size() > 0) {
                for (LeaveData leaveData : list) {
                    String startTimeed = leaveData.getStartTime();//请假开始时间
                    String endTimeed = leaveData.getEndTime();//请假结束时间
                    //计算两个日期之间间隔
                    String bothTime = DateFormat.getBothDate(startTimeed, endTimeed);
                    leaveData.setBothTime(bothTime);
                    Integer status = leaveData.getStatus();
                    String review = leaveData.getReview();
                    String reviewRemark = leaveData.getReviewRemark();
                    String reviewTime = leaveData.getReviewTime();
                    if (status == 2) {
                        if (review != null) {
                            int index = review.indexOf("=1");
                            if (index == -1) {
                                leaveData.setReviewResult("通过");
                            } else {
                                leaveData.setReviewResult("未通过");
                            }
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
                        if (reviewRemarkArr != null && !reviewRemarkArr.equals("")) {
                            for (String str : reviewRemarkArr) {
                                String[] reviewArr1 = str.split("=");
                                String name = empName.get(Integer.parseInt(reviewArr1[0]));
                                if (reviewArr1.length == 2) {
                                    reviewedRemark += name + "=" + reviewArr1[1] + ",";
                                }
                            }
                        }
                        if (!reviewedRemark.equals("")) {
                            leaveData.setReviewRemark(reviewedRemark);
                        }
                        if (reviewTimeArr != null && !reviewTimeArr.equals("")) {
                            for (String str : reviewTimeArr) {
                                String[] reviewArr1 = str.split("=");
                                String name = empName.get(Integer.parseInt(reviewArr1[0]));
                                if (reviewArr1.length == 2) {
                                    reviewedTime += name + "=" + reviewArr1[1] + ",";
                                }
                            }
                        }
                        if (!reviewedTime.equals("")) {
                            leaveData.setReviewTime(reviewedTime);
                        }
                    }
                }
            }
            result = new Result();
            result.setCode(0);
            result.setData(list);
            result.setCount(total.size());
        } catch (Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(Type.ERROR);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 下拉框员工
     *
     * @return
     */
    @RequiresPermissions("可选请假人")
    @RequestMapping("/getEmployeeName")
    @ResponseBody
    public String getEmployeeName() {
        Users users = this.getPrincipal();
        List<Map<String, String>> list = null;
        Integer empId = users.getEmployeeId();
        String empIdStr = "";
        List<String > employeeIdList=new ArrayList<>();

        List<Employee> rootList = employeeService.getEmployeeByManager(empId);

        List<Employee> empList = employeeService.getEmployeeByManager(0);
        ListUtils.getChildEmployeeId(rootList,empList,employeeIdList,null);

        for (String employeeIdStr : employeeIdList) {
            empIdStr+=employeeIdStr+",";
        }
        list = employeeService.getEmployeeNameMapByManager(empIdStr);
        return JSON.toJSONString(list);
    }


    /**
     * 获取请假配置项信息
     *
     * @return
     */
    @RequestMapping("/getLeaveNameMap")
    @ResponseBody
    public String getLeaveNameMap() {
        List<Map> list = leaveService.getConfigureMap();
        return JSON.toJSONString(list);
    }

    @RequiresPermissions(value = {"请假查询"}, logical = OR)
    @RequestMapping(value = "/addLeaveData", method = {RequestMethod.POST})
    @ResponseBody
    public String addLeaveData(@RequestBody LeaveData leaveData) {
        Users user = this.getPrincipal();
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
            if (user != null) {
                leaveData.setEmployeeId(user.getEmployeeId());
            }
        }
        employeeId = leaveData.getEmployeeId();//请假人

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
        if (user != null) {
            //获取请假人的部门
            Users users = userService.getUserByEmpId(employeeId);
            if (users != null) {
                Integer departmentId = users.getDepartmentId();
                leaveData.setDepartmentId(departmentId);
            }
            leaveData.setCreatedBy(user.getEmployeeId());
        }
        //设置下一个审核人
        Employee employee = employeeService.getEmployeeById(employeeId + "");
        if (employee != null) {
            Integer manager = employee.getManager();
            if (manager != null && !employee.getManager().equals("")) {
                leaveData.setNextManager(manager);
            } else {
                //返回结果:请添加绩效管理人后再申请
                return JSON.toJSONString(Type.AddManager);
            }
        }
        leaveData.setStatus(0);//设置请假记录状态为待审核
        leaveData.setReview("");
        leaveData.setReviewTime("");
        leaveData.setReviewRemark("");
        leaveData.setCreated(DateFormat.getYMDHMS(new Date()));
        int result = leaveService.addLeaveData(leaveData);
        if (result > 0) {
            return JSON.toJSONString(Type.SUCCESS);
        }
        return JSON.toJSONString(Type.ERROR);
    }

    @RequiresPermissions(value = {"请假查询"}, logical = OR)
    @RequestMapping("/updateLeaveData")
    @ResponseBody
    public String updateLeaveData(HttpServletRequest request) {
        String id = request.getParameter("id");
        String review = request.getParameter("review");//审核0:同意;1驳回
        String reviewRemark = request.getParameter("reviewRemark");//审核意见
        Users users = this.getPrincipal();

        Integer employeeId = null;//请假人
        Integer manager1 = null;//上一级绩效管理人
        Integer manager = null;//当前绩效管理人
        if (id != null) {
            LeaveData leaveData = leaveService.getLeadData(id);
            if (leaveData != null && leaveData.getId() != 0) {
                manager = leaveData.getNextManager();
                //请假人
                employeeId = leaveData.getEmployeeId();
                //获取上一级绩效管理人
                Integer empId = leaveData.getNextManager();
                Employee employee1 = employeeService.getEmployeeById(empId + "");
                if (employee1 != null) {
                    manager1 = employee1.getManager();
                }
                //审核
                if (review != null) {
                    String leaReview = leaveData.getReview();
                    if (leaReview == null) {
                        leaReview = "";
                    }
                    String reviewEmpId = "";
                    reviewEmpId = manager + "=" + review + ",";
                    leaReview += reviewEmpId;
                    leaveData.setReview(leaReview);
                }
                //审核意见
                if (reviewRemark != null) {
                    String leaReviewRemark = leaveData.getReviewRemark();
                    if (leaReviewRemark == null) {
                        leaReviewRemark = "";
                    }
                    String reviewRemarkEmpId = "";
                    reviewRemarkEmpId = manager + "=" + reviewRemark + ",";
                    leaReviewRemark += reviewRemarkEmpId;
                    leaveData.setReviewRemark(leaReviewRemark);
                }
                //获取审核时间
                String leaReviewTime = leaveData.getReviewTime();
                if (leaReviewTime == null) {
                    leaReviewTime = "";
                }
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
                    if (manager1 != null && !manager1.equals("")) {
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
                    return JSON.toJSONString(Type.SUCCESS);
                }
                return JSON.toJSONString(Type.ERROR);
            } else {
                return JSON.toJSONString(Type.ERROR);
            }
        } else {
            return JSON.toJSONString(Type.ERROR);
        }
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
    public String searchLeaveDataList(HttpServletRequest request) {
        String startTime = request.getParameter("startTime");
        String employeeId = request.getParameter("employeeId");
        String page = request.getParameter("page");
        String pageSize = request.getParameter("limit");
        Subject subject = SecurityUtils.getSubject();
        Users user = (Users) subject.getPrincipal();
        Integer departmentId = user.getDepartmentId();

        int rows = Page.getOffSet(page, pageSize);
        Map map = new HashMap();
        if ((startTime != null && !startTime.equals(""))) {
            map.put("startTime", startTime + "-01");
        }
        if (departmentId != null) {
            map.put("departmentId", departmentId);
        }
        if (employeeId != null && !employeeId.equals("")) {
            map.put("employeeId", employeeId);
        }
        List<LeaveData> total = leaveService.getLeaveDataList(map);
        map.put("page", rows);
        map.put("pageSize", pageSize);
        List<LeaveData> list = leaveService.getLeaveDataList(map);
        Result result = null;
        result = new Result();
        result.setCode(0);
        result.setData(list);
        result.setCount(total.size());
        return JSON.toJSONString(result);
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
    public String getLeaveDataStatisticsList(HttpServletRequest request) {
        String month = request.getParameter("month");
        String page = request.getParameter("page");
        String pageSize = request.getParameter("limit");
        int rows = Page.getOffSet(page, pageSize);

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
        if (month != null && !month.equals("")) {
            map.put("month", month + "-01");
        } else {
            month = DateFormat.getYMD();
            map.put("month", month);
        }

        Subject subject = SecurityUtils.getSubject();
        Users user = (Users) subject.getPrincipal();
        String empId=String.valueOf(user.getEmployeeId());
        boolean selectAllFlag = subject.isPermitted("员工信息查询所有");
        Integer employeeId = user.getEmployeeId();
        System.out.println("selectAllFlag::"+selectAllFlag);
        if (selectAllFlag) {
            employeeId = 0;
        }

        String empIdStr = "";
        List<String > employeeIdList=new ArrayList<>();

        List<Employee> rootList = employeeService.getEmployeeByManager(employeeId);

        List<Employee> empList = employeeService.getEmployeeByManager(0);
        ListUtils.getChildEmployeeId(rootList,empList,employeeIdList,null);

        map = new HashMap();
        for (String employeeIdStr : employeeIdList) {
            empIdStr+=employeeIdStr+",";
        }
        map.put("empIdStr", empIdStr);
        List<LeaveData> total = leaveService.getLeaveDataStatisticsList(map);
        map.put("page", rows);
        map.put("pageSize", pageSize);
        List<LeaveData> list = leaveService.getLeaveDataStatisticsList(map);

        Result result = new Result();
        result.setCount(total.size());
        result.setData(list);
        result.setCode(0);

        return JSON.toJSONString(result);
    }

}