package com.workattendance.controller;

import com.alibaba.fastjson.JSONObject;
import com.workattendance.pojo.MaintenanceRecord;
import com.workattendance.service.ExaminationService;
import com.workattendance.util.EasyuiResult;
import com.workattendance.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.html.HTMLHtmlElement;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wa/attendance")
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
    @ResponseBody
    @RequestMapping("/showLaborAll")
    public EasyuiResult showLaborAll(HttpServletRequest request) {
        String userName=request.getParameter("userName");
        String MonthDate=request.getParameter("MonthDate");
        String DayDateT=request.getParameter("DayDateT");
        String page=request.getParameter("page");
        String rows=request.getParameter("rows");
        int offset=Page.getOffSet(page,rows);
        List<MaintenanceRecord> maintenanceRecords = null;
        int count=0;
        Map map=new HashMap<>();
        if (MonthDate != "" && DayDateT != "") {
            map.put("userName",userName);
            map.put("DayDateT",DayDateT);
            List<MaintenanceRecord> listTotal= examinationServive.showLaborByDay(map);
            count=listTotal.size();
            map.put("page",offset);
            map.put("pageSize",rows);
            maintenanceRecords = examinationServive.showLaborByDay(map);
        } else if (MonthDate != "" && DayDateT == "") {
            map.put("userName",userName);
            map.put("MonthDate",MonthDate);
            List<MaintenanceRecord> listTotal = examinationServive.showLaborByMonth(map);
            count=listTotal.size();
            map.put("page",offset);
            map.put("pageSize",rows);
            maintenanceRecords= examinationServive.showLaborByMonth(map);
        } else if (MonthDate == "" && DayDateT == "") {
            map.put("userName",userName);
            List<MaintenanceRecord> listTotal = examinationServive.showLaborAll(map);
            count=listTotal.size();
            map.put("page",offset);
            map.put("pageSize",rows);
            maintenanceRecords=examinationServive.showLaborAll(map);
        }

        EasyuiResult easyuiResult=new EasyuiResult();
        easyuiResult.setTotal(count);
        easyuiResult.setRows(maintenanceRecords);
        return easyuiResult;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }
}
