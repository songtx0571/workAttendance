package com.howei.controller;

import com.howei.pojo.Employee;
import com.howei.pojo.OperatingHours;
import com.howei.pojo.OverhaulRecord;
import com.howei.pojo.Users;
import com.howei.service.DepartmentService;
import com.howei.service.EmployeeService;
import com.howei.service.WorkingService;
import com.howei.util.DateFormat;
import com.howei.util.ListUtils;
import com.howei.util.Result;
import com.howei.util.Type;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
     * @param month     月份
     * @param projectId 项目部
     * @return
     */
    @RequestMapping("getOperatingHoursList")
    public Result getOperatingHoursList(String month, Integer projectId) {
        Subject subject = SecurityUtils.getSubject();
        Users users = (Users) subject.getPrincipal();
        Result result = new Result();
        List<Map<String, Object>> resultList = new ArrayList<>();
        //登录信息失效
        if (users == null) {
            result.setCode(0);
            result.setMsg(Type.noUser.toString());
            result.setData(new ArrayList<>());
            return result;
        }

        Integer employeeId = users.getEmployeeId();//请假人:请假人为空即为当前登录人
        String empIdStr = "";//拼接请假人字符串
        //根据绩效管理人获取被绩效管理人
        List<String > employeeIdList=new ArrayList<>();

        List<Employee> rootList = employeeService.getEmployeeByManager(employeeId);

        List<Employee> empList = employeeService.getEmployeeByManager(0);
        ListUtils.getChildEmployeeId(rootList,empList,employeeIdList,null);

        for (String employeeIdStr : employeeIdList) {
            empIdStr+=employeeIdStr+",";
        }
        //去除剩余 ','
        if (empIdStr != null && !empIdStr.equals("")) {
            empIdStr = empIdStr.substring(0, empIdStr.lastIndexOf(","));
        }

        //遍历集合，整理返回此月数据
        String[] empArr = empIdStr.split(",");
        Map<String,Double> mapDayData=null;//初始化日期
        for (int k = 0; k < empArr.length; k++) {
            //获取此月天数
            int day = DateFormat.getDaysOfMonth(month + "-01");
            result.setCount(day);
            //初始化日期Map
            mapDayData=new HashMap<>();//初始化日期
            mapDayData=this.defaultMothData(mapDayData,day);
            Map<String, Object> map1 = new HashMap<>();
            String emp = empArr[k];
            //获取此人指定月份的运行工时数据
            Map map = new HashMap();
            map.put("monthDay", month + "-01");
            map.put("empIdStr", emp);
            map.put("projectId", projectId);
            List<OperatingHours> list = workingService.getOperatingHoursList(map);
            int size = 0;
            if (list != null && list.size() > 0) {
                size = list.size();
            } else {
                continue;
            }
            //获取此月（1-31天）数据
            int workAttendance = 0;//考勤天数
            double workingTotal = 0.0;//此月总共工时
            for (int i = 0; i < day; i++) {
                if (i < size) {
                    OperatingHours operatingHours = list.get(i);
                    double workingTime = operatingHours.getWorkingTime();//工时
                    String monthDay=operatingHours.getMonthDay();
                    String[] arr=monthDay.split("-");
                    mapDayData.put(arr[2],workingTime);
                    //考勤天数:工时不为o
                    if (workingTime > 0) {
                        workAttendance++;
                    }
                    //此月总共工时
                    workingTotal += workingTime;
                    map1.put("employeeNumber", operatingHours.getEmployeeNumber());//员工编号
                    map1.put("employeeName", operatingHours.getEmployeeName());//员工名称
                }
            }
            map1.put("workAttendance", workAttendance);
            map1.put("data", mapDayData);//1-31天数据
            //本月要求工时数
            BigDecimal bd = new BigDecimal(40 / 7 * day);
            double thisMonthRequirementTime = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            map1.put("monthTime", workingTotal);//本月工时
            //判断是否有加班工时
            if (workingTotal > thisMonthRequirementTime) {
                bd = new BigDecimal(workingTotal - thisMonthRequirementTime);
                double workOvertime = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                map1.put("workOvertime", workOvertime);//加班工时
            } else {
                map1.put("workOvertime", 0);//加班工时
            }
            resultList.add(map1);
        }
        result.setData(resultList);
        return result;
    }

    //设置当月数据默认为0
    private Map defaultMothData(Map map,int day) {
        DecimalFormat df = new DecimalFormat("00");
        for (Integer i = 1; i <= day; i++) {
            map.put(df.format(i), 0D);
        }
        return map;
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
            return Result.fail("用户失效");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        if (date == null) {
            date = sdf.format(new Date());
        }

        Map<String, Object> paramsMap = new HashMap<>();
        if (date != null) {
            paramsMap.put("date", date);
        }
        if (departmentId == null && !subject.isPermitted("查询所有部门")) {
            departmentId = users.getDepartmentId();
        }
        if (departmentId != null) {
            paramsMap.put("departmentId", departmentId);
        }
        List<String> employeeIdList = new ArrayList<>();
        List<String> userNumberList = new ArrayList<>();

        Integer usersEmployeeId = users.getEmployeeId();
        //根据id查询被管理人的员工信息

        List<Employee> rootList = employeeService.getEmployeeByManager(usersEmployeeId);

        List<Employee> empList = employeeService.getEmployeeByManager(0);
        ListUtils.getChildEmployeeId(rootList,empList,employeeIdList,userNumberList);
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
        for (OverhaulRecord overhaulRecord : overhaulRecordList) {
            Integer employeeId = overhaulRecord.getEmployeeId();
            Map<String, Object> mapMapMap;
            Map<String, Object> mapMap;
            if (!resultMap.containsKey(employeeId.toString())) {
                mapMapMap = new HashMap<>();
                mapMap = this.initMap(daysOfMonth);
                Map<String, Object> map = (Map<String, Object>) mapMap.get(overhaulRecord.getFinishDay());
                map.put("total", df.format(Double.valueOf(map.get("total").toString()) + (overhaulRecord.getWorkingHour() == null ? 0 : overhaulRecord.getWorkingHour())));
                List detailList = (ArrayList) map.get("detail");
                detailList.add(overhaulRecord);
                map.put("detail",detailList );
                mapMap.put(overhaulRecord.getFinishDay(), map);
                mapMapMap.put("data", mapMap);

                mapMapMap.put("all", overhaulRecord.getWorkingHour() == null ? 0 : df.format(overhaulRecord.getWorkingHour()));
                mapMapMap.put("over", overhaulRecord.getOvertime() == null ? 0 : df.format(overhaulRecord.getOvertime()));

                mapMapMap.put("employeeId", employeeId);
                mapMapMap.put("userName", overhaulRecord.getUserName());
                mapMapMap.put("userNumber", overhaulRecord.getUserNumber());
            } else {
                mapMapMap = (Map<String, Object>) resultMap.get(employeeId.toString());
                mapMap = (Map<String, Object>) mapMapMap.get("data");
                Map<String, Object> map = (Map<String, Object>) mapMap.get(overhaulRecord.getFinishDay());
                map.put("total", df.format(Double.valueOf(map.get("total").toString()) + (overhaulRecord.getWorkingHour() == null ? 0 : overhaulRecord.getWorkingHour())));
                List detailList = (ArrayList) map.get("detail");
                detailList.add(overhaulRecord);
                map.put("detail",detailList );
                mapMap.put(overhaulRecord.getFinishDay(), map);

                mapMapMap.put("data", mapMap);
                mapMapMap.put("all", df.format(overhaulRecord.getWorkingHour() == null ? 0 : overhaulRecord.getWorkingHour() + Double.valueOf(mapMapMap.get("all").toString())));
                mapMapMap.put("over", df.format((overhaulRecord.getOvertime() == null ? 0 : overhaulRecord.getOvertime()) + Double.valueOf(mapMapMap.get("over").toString())));
            }
            resultMap.put(employeeId.toString(), mapMapMap);
        }
        return Result.ok(daysOfMonth, resultMap.values());
    }

    //初始化当月
    private Map<String, Object> initMap(int n) {
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





    /**----------------------------------下拉框------------------------------------------*/

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

}
