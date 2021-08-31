package com.howei.controller;

import com.howei.pojo.*;
import com.howei.service.DepartmentService;
import com.howei.service.EmployeeService;
import com.howei.service.UserService;
import com.howei.service.WorkingService;
import com.howei.util.*;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/wa/working")
public class WorkingHoursController {

    @Autowired
    private WorkingService workingService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserService userService;

    /**
     * 运行工时
     *
     * @return
     */
    @RequestMapping("/tooperatingHours")
    public ModelAndView toOperatingHours() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("operatingHours");
        return modelAndView;
    }

    /**
     * 检修工时
     *
     * @return
     */
    @RequestMapping("/toOverhaulHours")
    public ModelAndView toOverHaulHours() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("overhaulHour");
        return modelAndView;
    }

    /**
     * 管理考勤
     *
     * @return
     */
    @RequestMapping("/toManageAtten")
    public ModelAndView toManageAtten() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("manageAttendance");
        return modelAndView;
    }


    /**
     * @param date         月份
     * @param departmentId 项目部
     * @return
     */
    @RequestMapping("getOperatingHoursList")
    public Result getOperatingHoursList(String date, Integer departmentId) {
        Subject subject = SecurityUtils.getSubject();
        Users users = (Users) subject.getPrincipal();
        Result result = new Result();
        List<Map<String, Object>> resultList = new ArrayList<>();
        //登录信息失效
        if (users == null) {
            return Result.fail(Type.noUser.toString());
        }

        Integer employeeId = users.getEmployeeId();//请假人:请假人为空即为当前登录人
        //根据绩效管理人获取被绩效管理人
        List<String> employeeIdList = new ArrayList<>();
        employeeIdList.add(employeeId.toString());
        List<Employee> rootList = employeeService.getEmployeeByManager(employeeId);

        List<Employee> empList = employeeService.getEmployeeByManager(0);

        ListUtils.getChildEmployeeId(rootList, empList, employeeIdList, null);

        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        //获取此月天数
        int day = DateFormat.getDaysOfMonth(date + "-01");
        result.setCount(day);
        if (employeeIdList.size() > 0) {
            for (String emp : employeeIdList) {
                //初始化日期Map
                Map<String, Object> mapDayData = this.defaultMothData(day);//初始化日期
                Map<String, Object> map1 = new HashMap<>();
                //获取此人指定月份的运行工时数据
                Map map = new HashMap();
                map.put("monthDay", date + "-01");
                map.put("empIdStr", emp);
                map.put("projectId", departmentId);
                List<OperatingHours> list = workingService.getOperatingHoursList(map);
                if (list == null || list.size() == 0) {
                    continue;
                }
                //获取此月（1-31天）数据
                int workAttendance = 0;//考勤天数
                double workingTotal = 0.0;//此月总共工时
                for (OperatingHours operatingHours : list) {
                    Map<String, Object> dailyDataMap = new HashMap<>();
                    double workingTime = operatingHours.getWorkingTime();//工时
                    dailyDataMap.put("total", workingTime);
                    dailyDataMap.put("detail", operatingHours);
                    String monthDay = operatingHours.getMonthDay();
                    String[] arr = monthDay.split("-");
                    mapDayData.put(arr[2], dailyDataMap);
                    //考勤天数:工时不为o
                    if (workingTime > 0) {
                        workAttendance++;
                    }
                    //此月总共工时
                    workingTotal += workingTime;
                    map1.put("employeeId", operatingHours.getEmployeeId());
                    map1.put("userNumber", operatingHours.getEmployeeNumber());//员工编号
                    map1.put("userName", operatingHours.getEmployeeName());//员工名称

                }
                map1.put("workAttendance", workAttendance);
                map1.put("data", mapDayData);//1-31天数据
                //本月要求工时数
                BigDecimal bd = new BigDecimal(40 / 7.0 * day);
                double thisMonthRequirementTime = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                map1.put("workingHoursTotal", decimalFormat.format(workingTotal));//本月工时
                //判断是否有加班工时
                if (workingTotal > thisMonthRequirementTime) {
                    bd = new BigDecimal(workingTotal - thisMonthRequirementTime);
                    double workOvertime = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    map1.put("workingOvertimeTotal", decimalFormat.format(workOvertime));//加班工时
                } else {
                    map1.put("workingOvertimeTotal", 0);//加班工时
                }
                resultList.add(map1);
            }

        }
        result.setData(resultList);
        return result;
    }


    /**
     * 检修工时统计
     *
     * @return
     */
    @GetMapping("/getOverhaulHours")
    public Result getOverHaulHours(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Integer departmentId
    ) {
        Subject subject = SecurityUtils.getSubject();
        Users users = (Users) subject.getPrincipal();
        if (users == null) {
            return Result.fail(Type.noUser.toString());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        if (date == null) {
            date = sdf.format(new Date());
        }

        Map<String, Object> paramsMap = new HashMap<>();
        if (date != null) {
            paramsMap.put("date", date);
        }
        if (departmentId != null) {
            paramsMap.put("departmentId", departmentId);
        }
        List<String> employeeIdList = new ArrayList<>();
        employeeIdList.add(String.valueOf(users.getEmployeeId()));
        List<String> userNumberList = new ArrayList<>();
        userNumberList.add(users.getUserNumber());

        Integer usersEmployeeId = users.getEmployeeId();
        //根据id查询被管理人的员工信息

        List<Employee> rootList = employeeService.getEmployeeByManager(usersEmployeeId);

        List<Employee> empList = employeeService.getEmployeeByManager(0);
        ListUtils.getChildEmployeeId(rootList, empList, employeeIdList, userNumberList);
        if (employeeIdList.size() > 0) {
            paramsMap.put("employeeIdList", employeeIdList);
        }
        if (userNumberList.size() > 0) {
            paramsMap.put("userNumberList", userNumberList);
        }

        List<OverhaulRecord> overhaulRecordList = new ArrayList<>();
        //查询维护记录
        List<OverhaulRecord> maintainRecordList = workingService.getMaintainRecordByMap(paramsMap);
        if (maintainRecordList != null && maintainRecordList.size() > 0) {
            overhaulRecordList.addAll(maintainRecordList);
        }
        //查询缺陷记录
        List<OverhaulRecord> defectList = workingService.getDefectByMap(paramsMap);
        if (defectList != null && defectList.size() > 0) {
            overhaulRecordList.addAll(defectList);
        }
        //查询检修记录
        List<OverhaulRecord> maintenanceRecordList = workingService.getMaintenceRecordByMap(paramsMap);
        if (maintenanceRecordList != null && maintenanceRecordList.size() > 0) {
            overhaulRecordList.addAll(maintenanceRecordList);
        }
        int daysOfMonth = DateFormat.getDaysOfMonth(date + "-1");
        Map<String, Object> resultMap = new HashMap<>();
        //循环每一条记录,
        DecimalFormat df = new DecimalFormat("0.0");
        double baseWorkHours = new BigDecimal(40 / 7.0 * daysOfMonth).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        //循环list
        for (OverhaulRecord overhaulRecord : overhaulRecordList) {
            Integer employeeId = overhaulRecord.getEmployeeId();
            Map<String, Object> mapMapMap;
            Map<String, Object> mapMap;
            //如果map中不存在则添加该条数据,存在则获取数据,在对应的日期中添加工时
            if (!resultMap.containsKey(employeeId.toString())) {
                mapMapMap = new HashMap<>();
                mapMap = this.defaultMothData(daysOfMonth);
                Map<String, Object> map = (Map<String, Object>) mapMap.get(overhaulRecord.getFinishDay());
                map.put("total", df.format(Double.valueOf(map.get("total").toString()) + (overhaulRecord.getWorkingHour() == null ? 0 : overhaulRecord.getWorkingHour())));
                List detailList = (ArrayList) map.get("detail");
                detailList.add(overhaulRecord);
                map.put("detail", detailList);
                mapMap.put(overhaulRecord.getFinishDay(), map);
                mapMapMap.put("data", mapMap);

                //本月工时
                mapMapMap.put("workingHoursTotal", overhaulRecord.getWorkingHour() == null ? 0 : df.format(overhaulRecord.getWorkingHour()));
                //加班工时
                mapMapMap.put("workingOvertimeTotal", overhaulRecord.getOvertime() == null ? 0 : df.format(overhaulRecord.getOvertime()));

                mapMapMap.put("employeeId", employeeId);
                mapMapMap.put("username", overhaulRecord.getUserName());
                mapMapMap.put("userNumber", overhaulRecord.getUserNumber());
                mapMapMap.put("workAttendance", 1);
            } else {
                mapMapMap = (Map<String, Object>) resultMap.get(employeeId.toString());
                mapMap = (Map<String, Object>) mapMapMap.get("data");
                Map<String, Object> map = (Map<String, Object>) mapMap.get(overhaulRecord.getFinishDay());
                if ("0".equals(map.get("total").toString())) {
                    mapMapMap.put("workAttendance", Integer.valueOf(mapMapMap.get("workAttendance").toString()) + 1);
                }
                map.put("total", df.format(Double.valueOf(map.get("total").toString()) + (overhaulRecord.getWorkingHour() == null ? 0 : overhaulRecord.getWorkingHour())));
                List detailList = (ArrayList) map.get("detail");
                detailList.add(overhaulRecord);
                map.put("detail", detailList);
                mapMap.put(overhaulRecord.getFinishDay(), map);

                mapMapMap.put("data", mapMap);
                double allWorkHours = overhaulRecord.getWorkingHour() == null ? 0 : overhaulRecord.getWorkingHour() + Double.valueOf(mapMapMap.get("workingHoursTotal").toString());

                double restHours = 0;
                if (allWorkHours >= baseWorkHours) {
                    restHours = allWorkHours - baseWorkHours;
                    allWorkHours = baseWorkHours;
                }
                mapMapMap.put("workingHoursTotal", df.format(allWorkHours));
                mapMapMap.put("workingOvertimeTotal", df.format(restHours + (overhaulRecord.getOvertime() == null ? 0 : overhaulRecord.getOvertime()) + Double.valueOf(mapMapMap.get("workingOvertimeTotal").toString())));
            }
            resultMap.put(employeeId.toString(), mapMapMap);
        }
        List values = resultMap.values().stream().collect(Collectors.toList());
        return Result.ok(daysOfMonth, values);
    }

    //初始化当月
    private Map<String, Object> defaultMothData(int n) {
        Map<String, Object> resultMap = new HashMap<>();
        DecimalFormat df = new DecimalFormat("00");
        for (Integer i = 1; i <= n; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("total", 0);
            map.put("detail", new ArrayList<>());
            resultMap.put(df.format(i), map);
        }
        return resultMap;
    }


    /**
     * 部门下拉框列表
     *
     * @return
     */
    @RequestMapping("getDepMap")
    public Result getDepMap() {
        Result result = new Result();
        List<Map<String, String>> map = departmentService.getDepMap();
        result.setData(map);
        result.setCode(0);
        return result;
    }

    /**
     * 管理考勤 数据
     *
     * @param date 年月
     * @return
     */

    @GetMapping("/getManagerWorkingHours")
    public Result getManagerHours(
            @RequestParam(required = false) String date
    ) {
        Subject subject = SecurityUtils.getSubject();
        Users loginUser = (Users) subject.getPrincipal();
        //登录信息失效
        if (loginUser == null) {
            return Result.fail(Type.noUser.toString());
        }

        Integer loginUserEmployeeId = loginUser.getEmployeeId();
        //根据绩效管理人获取被绩效管理人
        List<String> employeeIdList = new ArrayList<>();

        employeeIdList.add(loginUserEmployeeId.toString());

        List<Employee> rootList = employeeService.getEmployeeByManager(loginUserEmployeeId);

        List<Employee> empList = employeeService.getEmployeeByManager(0);

        ListUtils.getChildEmployeeId(rootList, empList, employeeIdList, null);
        Result result = new Result();
        //获取此月天数
        int dayCount = DateFormat.getDaysOfMonth(date + "-01");
        result.setCount(dayCount);
        List<Map<String, Object>> resultMapList = new ArrayList<>();
        for (String employeeId : employeeIdList) {
            int workAttendance = 0;//考勤天数
            double workingHoursTotal = 0.0;//此月总共工时
            Map paramMap = new HashMap();
            paramMap.put("employeeId", employeeId);
            Users userByEmpId = userService.getUserByEmpId(Integer.valueOf(employeeId));
            paramMap.put("departmentId", userByEmpId.getDepartmentId());
            paramMap.put("month", date);
            List<ManagerHours> managerHoursList = workingService.getManagerHoursListByMap(paramMap);
            if ((managerHoursList == null || managerHoursList.size() == 0) && !employeeId.equals(loginUserEmployeeId.toString())) {
                continue;
            }
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("employeeId", employeeId);
            resultMap.put("userNumber", userByEmpId.getUserNumber());
            resultMap.put("userName", userByEmpId.getUserName());
            Map<String, Object> mapDayData = this.defaultMothData(dayCount);//初始化日期
            for (ManagerHours managerHours : managerHoursList) {
                String monthDay = managerHours.getMonthDay();
                String day = monthDay.substring(monthDay.lastIndexOf("-") + 1);
                Map dayDataMap = (Map) mapDayData.get(day);
                Double workIngHour = managerHours.getWorkingHour() == null ? 0.0 : managerHours.getWorkingHour();
                if (managerHours.getType() == 1) {
                    workAttendance++;
                    workingHoursTotal += workIngHour;
                }
                dayDataMap.put("total", workIngHour);
                dayDataMap.put("detail", managerHours);
                mapDayData.put(day, dayDataMap);
            }

            resultMap.put("workAttendance", workAttendance);
            resultMap.put("workingHoursTotal", new BigDecimal(workingHoursTotal).setScale(1, BigDecimal.ROUND_HALF_UP));
            resultMap.put("data", mapDayData);
            resultMapList.add(resultMap);
        }
        return Result.ok(resultMapList.size(), resultMapList);

    }

    /**
     * 点击上下班
     *
     * @param employeeId
     * @param monthDay
     * @param type
     * @return
     */
    @PostMapping("/postManagerWorkingHours")
    public Result postWorkTime(
            @RequestParam(required = false) Integer employeeId,
            @RequestParam(required = false) String monthDay,
            @RequestParam(required = false) String type) {
        if (StringUtils.isEmpty(type)) {
            return Result.fail(Type.noParameters);
        }
        Subject subject = SecurityUtils.getSubject();
        Users loginUser = (Users) subject.getPrincipal();
        //登录信息失效
        if (loginUser == null) {
            return Result.fail(Type.noUser.toString());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        Integer departmentId = null;
        if (StringUtils.isEmpty(employeeId)) {
            employeeId = loginUser.getEmployeeId();
            departmentId = loginUser.getDepartmentId();
        } else {
            Users userByEmpId = userService.getUserByEmpId(employeeId);
            departmentId = userByEmpId.getDepartmentId();
        }
        if (monthDay == null) {
            monthDay = sdf1.format(new Date());
        }
        if ("0".equals(type)) {
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("employeeId", employeeId);
            paramsMap.put("monthDay", monthDay);
            paramsMap.put("type", 0);
            ManagerHours managerHours = workingService.getManagerHoursByMap(paramsMap);
            if (managerHours != null) {
                return Result.fail("已经上班打卡");
            }
            managerHours = new ManagerHours();
            managerHours.setDepartmentId(departmentId);
            managerHours.setEmployeeId(employeeId);
            managerHours.setMonthDay(monthDay);
            managerHours.setType(0);
            managerHours.setWorkStartTime(sdf.format(new Date()));
            workingService.insertManagerHours(managerHours);
        } else {
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("monthDay", monthDay);
            paramsMap.put("employeeId", employeeId);
            paramsMap.put("type", 0);
            ManagerHours managerHours = workingService.getManagerHoursByMap(paramsMap);
            String workStartTime = managerHours.getWorkStartTime();
            Date parseWorkStartTime = null;
            try {
                parseWorkStartTime = sdf.parse(workStartTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date newDate = new Date();
            managerHours.setType(1);
            managerHours.setWorkEndTime(sdf.format(newDate));
            Double workingHours = (newDate.getTime() - parseWorkStartTime.getTime()) / 1000 / 3600D;
            BigDecimal bd = new BigDecimal(workingHours);
            managerHours.setWorkingHour(bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
            workingService.updateManagerHours(managerHours);
        }
        return Result.ok();
    }

    /**
     * 获取登录用户的上下班状态
     *
     * @return
     */
    @GetMapping("/getManagerWorkingType")
    @ResponseBody
    public Result getManagerWorkStatus() {
        Subject subject = SecurityUtils.getSubject();
        Users loginUser = (Users) subject.getPrincipal();
        //登录信息失效
        if (loginUser == null) {
            return Result.fail(Type.noUser.toString());
        }
        Map<String, Object> paramMap = new HashMap<>();
        Integer employeeId = loginUser.getEmployeeId();
        paramMap.put("employeeId", employeeId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String monthDay = sdf.format(new Date());
        paramMap.put("monthDay", monthDay);
        ManagerHours managerHours = workingService.getManagerHoursByMap(paramMap);
        Map<String, Object> resultMap = new HashMap<>();
        if (managerHours == null) {
            resultMap.put("type", -1);
        } else {
            resultMap.put("type", managerHours.getType());
        }
        return Result.ok(1, resultMap);
    }


    /**
     * 保存考勤数据数据
     *
     * @param date
     * @param type 0运行考勤,1检修考勤,2管理考勤
     * @return
     */
    @GetMapping("/saveWorkingHour")
    @Transactional
    public Result saveWorkingHour(String date, String type, @RequestParam(required = false) Integer departmentId) {
        Subject subject = SecurityUtils.getSubject();
        Users loginUser = (Users) subject.getPrincipal();
        //登录信息失效
        if (loginUser == null) {
            return Result.fail(Type.noUser.toString());
        }
        Result middleResult = null;
        Map<String, Object> map = new HashMap<>();
        if ("0".equals(type)) {
            middleResult = getOperatingHoursList(date, departmentId);
        } else if ("1".equals(type)) {
            middleResult = getOverHaulHours(date, departmentId);
        } else {
            middleResult = getManagerHours(date);
        }

        List<Map<String, Object>> workingHourList = null;
        if (middleResult != null) {
            workingHourList = (List<Map<String, Object>>) middleResult.getData();
        }
        if (!CollectionUtils.isEmpty(workingHourList)) {
            for (Map<String, Object> workingHour : workingHourList) {
                workingHour.put("month", date);
                workingHour.put("type", type);
                workingService.insertWorkingHour(workingHour);
                workingService.updateWorkingHourById(workingHour);
            }
        }
        return Result.ok();
    }

    /**
     * 获取考勤天数
     *
     * @param date       日期 年月
     * @param employeeId 员工id
     * @return
     */
    @GetMapping("getWorkAttendance")
    public Result getWorkingHour(
            String date, Integer employeeId) {
        Subject subject = SecurityUtils.getSubject();
        Users loginUser = (Users) subject.getPrincipal();
        //登录信息失效
        if (loginUser == null) {
            return Result.fail(Type.noUser.toString());
        }
        if (StringUtils.isEmpty(date) || employeeId == null || employeeId == 0) {
            return Result.fail(ResultEnum.NO_PARAMETERS);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("date", date);
        map.put("employeeId", employeeId);
        List<Map<String, Object>> workingHourList = workingService.getWorkingHourByMap(map);
        int count = 0;
        if (workingHourList != null && workingHourList.size() > 0) {
            for (Map<String, Object> workingHourMap : workingHourList) {
                int workAttendance = (int) workingHourMap.get("workAttendance");
                if (count < workAttendance) {
                    count = workAttendance;
                }
            }
        }
        return Result.ok(1, count);

    }
}
