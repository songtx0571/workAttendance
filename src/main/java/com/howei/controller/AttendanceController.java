package com.howei.controller;

import com.howei.pojo.MaintenanceRecord;
import com.howei.service.ExaminationService;
import com.howei.util.EasyuiResult;
import com.howei.util.Page;
import com.howei.util.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.shiro.authz.annotation.Logical.OR;

@Controller
@RequestMapping("/wa/attendance")
//@CrossOrigin(origins="http://test.hopeop.com:80",allowCredentials = "true")
public class AttendanceController {

    @Autowired
    private ExaminationService examinationServive;

    private String attendanceType;//考勤查看类型

    /**
     * 员工工时
     * @param request
     * @return
     */
    @RequestMapping("/toworkHours")
    public String toworkHours(HttpServletRequest request){
        request.setAttribute("attendance","workHours");
        return "attendance";
    }

    /**
     * 工时统计
     * @param request
     * @return
     */
    @RequestMapping("/tohourStatistics")
    public String tohourStatistics(HttpServletRequest request){
        request.setAttribute("attendance","hourStatistics");
        return "attendance";
    }

    /**
     * 员工考勤
     * @param request
     * @return
     */
    @RequestMapping("/toempAttendance")
    public String toempAttendance(HttpServletRequest request){
        request.setAttribute("attendance","empAttendance");
        return "attendance";
    }

    // 获取工时信息
    @RequiresPermissions(value = {"员工工时"},logical = OR)
    @ResponseBody
    @RequestMapping("/showLaborAll")
    public Result showLaborAll(HttpServletRequest request) {
        String userName=request.getParameter("userName");
        String MonthDate=request.getParameter("MonthDate");
        String DayDateT=request.getParameter("DayDateT");
        String page=request.getParameter("page");
        String limit=request.getParameter("limit");
        int rows=Page.getOffSet(page,limit);
        List<MaintenanceRecord> maintenanceRecords = null;
        int count=0;
        Map map=new HashMap<>();
        if (MonthDate != "" && DayDateT != "") {
            map.put("userName",userName);
            map.put("DayDateT",DayDateT);
            List<MaintenanceRecord> listTotal= examinationServive.showLaborByDay(map);
            count=listTotal.size();
            map.put("pageSize",limit);
            map.put("page",rows);
            maintenanceRecords = examinationServive.showLaborByDay(map);
        } else if (MonthDate != "" && DayDateT == "") {
            map.put("userName",userName);
            map.put("MonthDate",MonthDate);
            List<MaintenanceRecord> listTotal = examinationServive.showLaborByMonth(map);
            count=listTotal.size();
            map.put("pageSize",limit);
            map.put("page",rows);
            maintenanceRecords= examinationServive.showLaborByMonth(map);
        } else if (MonthDate == "" && DayDateT == "") {
            map.put("userName",userName);
            List<MaintenanceRecord> listTotal = examinationServive.showLaborAll(map);
            count=listTotal.size();
            map.put("pageSize",limit);
            map.put("page",rows);
            maintenanceRecords=examinationServive.showLaborAll(map);
        }

        Result result=new Result();
        result.setCode(0);
        result.setData(maintenanceRecords);
        result.setCount(count);
        return result;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }
}
