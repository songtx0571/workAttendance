package com.howei.controller;

import com.howei.pojo.Employee;
import com.howei.pojo.OperatingHours;
import com.howei.pojo.Users;
import com.howei.service.DepartmentService;
import com.howei.service.EmployeeService;
import com.howei.service.WorkingService;
import com.howei.util.DateFormat;
import com.howei.util.Result;
import com.howei.util.Type;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("wa/working")
public class WorkingHoursController {

    @Autowired
    private WorkingService workingService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    /**
     * 运行工时
     * @return
     */
    @RequestMapping("/tooperatingHours")
    public ModelAndView toOperatingHours(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("operatingHours");
        return modelAndView;
    }

    /**
     *
     * @param month 月份
     * @param projectId 项目部
     * @return
     */
    @RequestMapping("getOperatingHoursList")
    public Result getOperatingHoursList(String month,Integer projectId){
        month="2021-06";
        projectId=17;
        Subject subject= SecurityUtils.getSubject();
        Users users=(Users)subject.getPrincipal();
        Result result=new Result();
        List<Map<String,Object>> resultList=new ArrayList<>();
        //登录信息失效
        if(users==null){
            result.setCode(0);
            result.setMsg(Type.noUser.toString());
        }

        //Integer employeeId = users.getEmployeeId();//请假人:请假人为空即为当前登录人
        Integer employeeId =230;
        String empIdStr = "";//拼接请假人字符串
        //根据绩效管理人获取被绩效管理人
        List<Employee> rootList = employeeService.getEmployeeByManager(employeeId);
        if (rootList != null) {
            empIdStr += employeeId + ",";
            List<Employee> empList = employeeService.getEmployeeByManager(0);
            for (Employee employee : rootList) {
                empIdStr += employee.getId() + ",";
                empIdStr += getUsersId(employee.getId(), empList);
            }
        }
        //去除剩余 ','
        if (empIdStr != null && !empIdStr.equals("")) {
            empIdStr = empIdStr.substring(0, empIdStr.lastIndexOf(","));
        }

        //遍历集合，整理返回此月数据
        String[] empArr=empIdStr.split(",");
        //
        for(int k=0;k<empArr.length;k++){
            //获取此月天数
            int day=DateFormat.getDaysOfMonth(month+"-01");
            Double[] dayArr=new Double[day];
            Map<String,Object> map1=new HashMap<>();
            String emp=empArr[k];
            //获取此人指定月份的运行工时数据
            Map map =new HashMap();
            map.put("monthDay",month+"-01");
            map.put("empIdStr",emp);
            map.put("projectId",projectId);
            List<OperatingHours> list=workingService.getOperatingHoursList(map);
            int size=0;
            if(list!=null && list.size()>0){
                size=list.size();
            }else{
                continue;
            }
            //获取此月（1-31天）数据
            int workAttendance=0;//考勤天数
            double workingTotal=0.0;//此月总共工时
            for (int i = 0; i < day; i++) {
                if(i<size){
                    OperatingHours operatingHours=list.get(i);
                    double workingTime=operatingHours.getWorkingTime();//工时
                    dayArr[i]=workingTime;
                    //考勤天数:工时不为o
                    if(workingTime>0){
                        workAttendance++;
                    }
                    //此月总共工时
                    workingTotal+=workingTime;
                    map1.put("employeeNumber",operatingHours.getEmployeeNumber());//员工编号
                    map1.put("employeeName",operatingHours.getEmployeeName());//员工名称
                }else {
                    dayArr[i]=0.0;
                }
            }
            map1.put("workAttendance",workAttendance);
            map1.put("data",dayArr);//1-31天数据
            //本月要求工时数
            BigDecimal bd = new BigDecimal(40/7*day);
            double thisMonthRequirementTime=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            map1.put("monthTime",thisMonthRequirementTime);//本月工时
            //判断是否有加班工时
            if(workingTotal>thisMonthRequirementTime){
                bd = new BigDecimal(workingTotal-thisMonthRequirementTime);
                double workOvertime=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                map1.put("workOvertime",workOvertime);//加班工时
            }else{
                map1.put("workOvertime",0);//加班工时
            }
            resultList.add(map1);
        }
        result.setData(resultList);
        return result;
    }

    public String getUsersId(Integer empId, List<Employee> empList) {
        List<String> result = new ArrayList<>();
        String userId = "";
        String usersId = "";
        for (Employee employee : empList) {
            if (employee.getManager() != null || employee.getManager() != 0) {
                if (employee.getManager().equals(empId)) {
                    usersId += employee.getId() + ",";
                    result.add(employee.getId() + "");
                }
            }
        }
        for (String str : result) {
            String userId1 = getUsersId(Integer.parseInt(str), empList);
            if (userId1 != null && !userId1.equals("")) {
                userId += userId1;
            }
        }
        if (userId != null && !userId.equals("null")) {
            usersId += userId;
        }
        return usersId;
    }


    /**----------------------------------下拉框------------------------------------------*/

    /**
     * 部门下拉框列表
     * @return
     */
    @RequestMapping("getDepMap")
    public Result getDepMap(){
        Result result=new Result();
        List<Map<String,String>> map=departmentService.getDepMap();
        result.setData(map);
        result.setCode(0);
        return result;
    }
}
