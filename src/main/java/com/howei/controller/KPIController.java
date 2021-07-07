package com.howei.controller;

import com.howei.pojo.*;
import com.howei.service.*;
import com.howei.util.DateFormat;
import com.howei.util.ListUtils;
import com.howei.util.Page;
import com.howei.util.Result;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.shiro.authz.annotation.Logical.OR;

@Controller
@RequestMapping("/wa/kpi")
@CrossOrigin(origins = "http://test.hopeop.com:80", allowCredentials = "true")
public class KPIController {

    @Autowired
    PostPeratorService postPeratorService;

    @Autowired
    PostPeratorDataService postPeratorDataService;

    @Autowired
    private InformService informService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExaminationService examinationServive;
    @Autowired
    private EmployeeService employeeService;

    /**
     * 跳转kpi页面
     *
     * @return
     */
    @RequestMapping("/tokpi")
    public String toKPI() {
        return "kpi";
    }

    @RequestMapping("/toInformKpi")
    public String toInformKpi() {
        return "informKpi";
    }

    @RequestMapping("/toWorkingHours")
    public String toWorkingHours() {
        return "workingHours";
    }


    @GetMapping("/toExamKnowledgeKpi")
    private String toExamKpiPage() {
        return "examKnowledgeKpi";
    }

    /**
     * 获取运行KPI数据
     *
     * @param request
     * @return
     */
    @RequiresPermissions(value = {"运行KPI查询"}, logical = OR)
    @RequestMapping("/getKPIList")
    @ResponseBody
    public Result getKPIList(HttpServletRequest request) {
        String startTime = request.getParameter("startTime");
        String departmentId = request.getParameter("departmentId");
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        int rows = Page.getOffSet(page, limit);
        Map map = new HashMap();
        if (departmentId != null && !"0".equals(departmentId.trim()) && !"".equals(departmentId.trim())) {
            map.put("departmentId", departmentId);
        }
        Subject subject = SecurityUtils.getSubject();
        Users users = (Users) subject.getPrincipal();
        boolean selectAllFlag = subject.isPermitted("员工信息查询所有");
        Integer employeeId = users.getEmployeeId();
        String empIdStr = "";
        if (selectAllFlag) {
            employeeId = 0;
        }
        List<String> employeeIdList = new ArrayList<>();
        employeeIdList.add(employeeId.toString());
        List<Employee> rootList = employeeService.getEmployeeByManager(employeeId);

        List<Employee> empList = employeeService.getEmployeeByManager(0);
        ListUtils.getChildEmployeeId(rootList, empList, employeeIdList, null);
        // map.put("empId", empIdStr);


        if (startTime != null && !startTime.equals("")) {
            map.put("startTime", startTime);
        } else {
            map.put("startTime", DateFormat.ThisMonth());
        }


        Long frequencySum = 0L;
        Long pointSum = 0L;


        Map<String, Object> mapResult = new HashMap<>();

        List<Map> list = postPeratorService.getKPIList(map);
        mapResult.put("list", list);

        Integer listSize = 1;
        if (list != null && list.size() > 0) {
            listSize = list.size();
        }


        for (Map kpiMap : list) {
            frequencySum += (Long) kpiMap.get("frequency");
            pointSum += (Long) kpiMap.get("point");
        }
        mapResult.put("frequencySum", frequencySum.toString());
        mapResult.put("pointSum", pointSum.toString());

        DecimalFormat df = new DecimalFormat("0.0");

        double frequencyAver = frequencySum * 1.0 / listSize;
        double pointAver = pointSum * 1.0 / listSize;
        mapResult.put("frequencyAverage", df.format(frequencyAver));
        mapResult.put("pointAverage", df.format(pointAver));


        double frequencyVariance = 0;
        double pointVariance = 0;

        for (Map kpiMap : list) {
            Long frequencyi = (Long) kpiMap.get("frequency");
            frequencyVariance += (frequencyi - frequencyAver) * (frequencyi - frequencyAver);
            Long pointi = (Long) kpiMap.get("point");
            pointVariance += (pointi - pointAver) * (pointi - pointAver);
        }
        frequencyVariance = Math.sqrt(frequencyVariance / listSize);
        pointVariance = Math.sqrt(pointVariance / listSize);

        mapResult.put("frequencyVariance", df.format(frequencyVariance));
        mapResult.put("pointVariance", df.format(pointVariance));


        Result result = new Result();
        result.setCode(0);
        result.setData(mapResult);
        result.setCount(list.size());
        return result;
    }


    /**
     * 跳转当月巡检次数页面
     *
     * @param userId
     * @return
     */
    @RequestMapping("/toFrequency")
    public ModelAndView toFrequency(@Param("userId") String userId, @Param("startTime") String startTime) {
        ModelAndView model = new ModelAndView();
        model.setViewName("frequency");
        model.addObject("userId", userId);
        model.addObject("startTime", startTime);
        return model;
    }

    /**
     * 查询
     *
     * @param request
     * @return
     */
    @RequestMapping("/getFrequencyList")
    @ResponseBody
    public Result getFrequencyList(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        String startTime = request.getParameter("startTime");
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        int rows = Page.getOffSet(page, limit);
        List<PostPerator> result = new ArrayList<>();
        int count = 0;
        if (userId != null) {
            Map map = new HashMap();
            map.put("postPeratorId", userId);
            if (startTime != null && !startTime.equals("")) {
                map.put("startTime", startTime);
            } else {
                map.put("startTime", DateFormat.ThisMonth());
            }
            result = postPeratorService.getFrequencyList(map);
            count = result.size();
            result.clear();
            map.put("pageSize", limit);
            map.put("page", rows);
            result = postPeratorService.getFrequencyList(map);
        }
        Result result1 = new Result();
        result1.setData(result);
        result1.setCount(count);
        result1.setCode(0);
        return result1;
    }

    /**
     * 跳转巡检点数页面
     *
     * @param userId
     * @param startTime
     * @return
     */
    @RequestMapping("/toPoint")
    public ModelAndView toPoint(@Param("userId") String userId, @Param("startTime") String startTime) {
        ModelAndView model = new ModelAndView();
        model.setViewName("point");
        model.addObject("userId", userId);
        model.addObject("startTime", startTime);
        return model;
    }

    /**
     * 查询巡检点数
     *
     * @param request
     * @return
     */
    @RequestMapping("/getPointList")
    @ResponseBody
    public Result getPointList(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        String startTime = request.getParameter("startTime");
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        int rows = Page.getOffSet(page, limit);
        List<PostPeratorData> result = new ArrayList<>();
        int count = 0;
        if (userId != null) {
            Map map = new HashMap();
            map.put("createdBy", userId);
            if (startTime != null && !startTime.equals("")) {
                map.put("startTime", startTime);
            } else {
                map.put("startTime", DateFormat.ThisMonth());
            }
            result = postPeratorDataService.getPointList(map);
            count = result.size();
            result.clear();
            map.put("pageSize", limit);
            map.put("page", rows);
            result = postPeratorDataService.getPointList(map);
        }
        Result result1 = new Result();
        result1.setData(result);
        result1.setCount(count);
        result1.setCode(0);
        return result1;
    }

    @RequiresPermissions(value = {"通知KPI查询"}, logical = OR)
    @RequestMapping("/getInformKPIList")
    @ResponseBody
    public Result getInformKPIList(HttpServletRequest request) {
        String startTime = request.getParameter("startTime");
        String departmentId = request.getParameter("departmentId");
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        int rows = Page.getOffSet(page, limit);
        Map map = new HashMap();

        if (departmentId != null && !"0".equals(departmentId.trim()) && !"".equals(departmentId.trim())) {
            map.put("departmentId", departmentId);
        }
        if (startTime != null && !startTime.equals("")) {
            map.put("startTime", startTime);
        } else {
            map.put("startTime", DateFormat.ThisMonth());
        }

        List<Map> listTotal = informService.getInformKPIList(map);
//        map.put("pageSize", limit);
//        map.put("page", rows);
        List<Map> list = informService.getInformKPIList(map);

        long createInformCountSum = 0;
        long selcountSum = 0;
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Map map1 = list.get(i);
                String userName = (String) map1.get("userName");
                long createdCount = (long) map1.get("createdCount");
                long selCount = (long) map1.get("selCount");
//                if (createdCount.isEmpty() || createdCount == null || createdCount.equals("null")) {
//                    map1.put("createdCount", 0);
//                    Users user = userService.findByUserName(userName);
//                    if (user != null) {
//                        map1.put("Id", user.getId());
//                        map1.put("Name", user.getUserName());
//                    } else {
//                        list.remove(map1);
//                    }
//                }


                createInformCountSum += createdCount;
                selcountSum += selCount;
            }
        }

        DecimalFormat df = new DecimalFormat("0.0");
        System.out.println("sum1:" + createInformCountSum);
        System.out.println("sum2:" + selcountSum);
        System.out.println("average1:" + df.format(createInformCountSum * 1.0 / list.size()));
        System.out.println("average1:" + df.format(selcountSum * 1.0 / list.size()));
        Result result = new Result();
        result.setCode(0);
        result.setData(list);
        result.setCount(listTotal.size());
        return result;
    }

    @RequestMapping("/toCreated")
    public ModelAndView toCreated(@Param("userId") String userId, @Param("startTime") String startTime) {
        ModelAndView model = new ModelAndView();
        model.setViewName("created");
        model.addObject("userId", userId);
        model.addObject("startTime", startTime);
        return model;
    }

    @RequestMapping("/toSel")
    public ModelAndView toSel(@Param("userId") String userId) {
        ModelAndView model = new ModelAndView();
        model.setViewName("sel");
        model.addObject("userId", userId);
        return model;
    }

    @RequestMapping("/getSelList")
    @ResponseBody
    public Result getSelList(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        int rows = Page.getOffSet(page, limit);
        List<Inform> result = new ArrayList<>();
        int count = 0;
        if (userId != null) {
            Map map = new HashMap();
            map.put("createdBy", userId);
            result = informService.getSelList(map);
            count = result.size();
            result.clear();
            map.put("pageSize", limit);
            map.put("page", rows);
            result = informService.getSelList(map);
        }
        Result result1 = new Result();
        result1.setData(result);
        result1.setCount(count);
        return result1;
    }


    @RequestMapping("/getCreatedList")
    @ResponseBody
    public Result getCreatedList(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        String startTime = request.getParameter("startTime");
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        int rows = Page.getOffSet(page, limit);
        List<Inform> result = new ArrayList<>();
        int total = 0;
        if (userId != null) {
            Map map = new HashMap();
            if (startTime != null && !startTime.equals("")) {
                map.put("startTime", startTime);
            } else {
                map.put("startTime", DateFormat.ThisMonth());
            }
            map.put("createdBy", userId);
            total = informService.getCreatedList(map).size();
            map.put("pageSize", limit);
            map.put("page", rows);
            result = informService.getCreatedList(map);
            if (result != null) {
                for (Inform inf : result) {
                    inf.getCreatedBy();
                }
            }
        }
        Result result1 = new Result();
        result1.setCode(0);
        result1.setData(result);
        result1.setCount(total);
        /*EasyuiResult easyuiResult=new EasyuiResult();
        easyuiResult.setRows(result);
        easyuiResult.setTotal(total);*/
        return result1;
    }

    @RequiresPermissions(value = {"检修KPI查询"}, logical = OR)
    @RequestMapping("/getWorkHoursList")
    @ResponseBody
    public Result getWorkHoursList(HttpServletRequest request) {
        String startTime = request.getParameter("startTime");
        String departmentId = request.getParameter("departmentId");
//        String page = request.getParameter("page");
//        String limit = request.getParameter("limit");
//        int rows = Page.getOffSet(page, limit);
        Map map = new HashMap();
        if (startTime != null && !startTime.equals("")) {
            map.put("startTime", startTime);
        } else {
            map.put("startTime", DateFormat.ThisMonth());
        }
        String depart = "0";
        if (departmentId != null && !"".equals(departmentId.trim())) {
            depart = departmentId;
        }
        List<MaintenanceRecord> list = examinationServive.getWorkingHoursByProPeople(map);
        //System.out.println("list::"+list);


        Subject subject = SecurityUtils.getSubject();
        Users user = (Users) subject.getPrincipal();
        boolean selectAllFlag = subject.isPermitted("员工信息查询所有");
        Integer employeeId = user.getEmployeeId();

        String empIdStr = "";
        if (selectAllFlag) {
            employeeId = 0;
        }
        List<String> employeeIdList = new ArrayList<>();
        employeeIdList.add(String.valueOf(user.getEmployeeId()));
        List<Employee> rootList = employeeService.getEmployeeByManager(employeeId);

        List<Employee> empList = employeeService.getEmployeeByManager(0);
        ListUtils.getChildEmployeeId(rootList, empList, employeeIdList, null);

        map = new HashMap();
        for (String employeeIdStr : employeeIdList) {
            empIdStr += employeeIdStr + ",";
        }
        map.put("empId", empIdStr);

        List<Map> users = userService.getNameByEmployeeIds(empIdStr);


        Integer size = users.size();
        //处理分页的users列表
//        List<Map> users2 = new ArrayList<>();
//        for (int i = rows; i < Integer.parseInt(page) * (Integer.parseInt(limit)); i++) {
//            if (i < users.size()) {
//                users2.add(users.get(i));
//            }
//        }
//        users = users2;

        List<Map<String, String>> result = resultWorkHoursList(users, list, depart, "", 0, "");

        Result result1 = new Result();
        result1.setCode(0);
        result1.setData(result);
        result1.setCount(size);
        return result1;
    }

    public List<Map<String, String>> resultWorkHoursList(List<Map> users, List<MaintenanceRecord> list, String depart, String rows, int offset, String page) {
        List<Map<String, String>> result = new ArrayList<>();
        double[] count = new double[users.size()];
        for (int i = 0; i < users.size(); i++) {
            Map<String, Object> userMap = users.get(i);
            count[i] = 0;
            for (int k = 0; k < list.size(); k++) {
                MaintenanceRecord record = list.get(k);
                String people = record.getPeople();
                String userNumber = userMap.get("userNumber").toString().toUpperCase();
                if (people.toUpperCase().contains(userNumber)) {
                    count[i] += record.getWorkingHours();
                }
            }
        }
        for (int i = 0; i < users.size(); i++) {

            Map<String, String> map = new HashMap<>();
            String departmentId = users.get(i).get("departmentId").toString();
            map.put("id", users.get(i).get("id").toString());
            map.put("name", users.get(i).get("userName").toString());
            map.put("userNumber", users.get(i).get("userNumber").toString());
            map.put("companyName", users.get(i).get("companyName").toString());
            map.put("departmentName", users.get(i).get("departmentName").toString());
            map.put("workingHours", count[i] + "");
            if (depart.equals(departmentId) || "0".equals(depart)) {
                result.add(map);
            }

        }
        return result;
    }

    @RequestMapping("/toSelWorkHours")
    public ModelAndView toSel(@Param("userId") String userId, @Param("startTime") String startTime, @Param("depart") String depart) {
        ModelAndView model = new ModelAndView();
        model.setViewName("selWorkHours");
        model.addObject("userId", userId);
        model.addObject("startTime", startTime);
        model.addObject("depart", depart);
        return model;
    }

    @RequestMapping("/getSelWorkHoursList")
    @ResponseBody
    public Result getSelWorkHoursList(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        String startTime = request.getParameter("startTime");
        List<Map<String, String>> resultList = new ArrayList<>();
        Map map = new HashMap();
        if (startTime != null && !startTime.equals("") && !startTime.equals(",")) {
            map.put("startTime", startTime);
        } else {
            startTime = DateFormat.ThisMonth();
            map.put("startTime", DateFormat.ThisMonth());
        }
        List<MaintenanceRecord> list = examinationServive.getWorkingHoursByProPeople(map);
        Users user = userService.findById(userId);
        int days = DateFormat.getDaysOfMonth(startTime + "-01");
        double[][] dayData = new double[days][1];
        String userNumber = user.getUserNumber().toUpperCase();
        for (int k = 0; k < list.size(); k++) {
            MaintenanceRecord record = list.get(k);
            String people = record.getPeople().toUpperCase();
            if (people.contains(userNumber)) {//包含
                String dateTimeStr = record.getDatetime();
                int dataTime = Integer.parseInt(dateTimeStr.substring(8, 10));
                dataTime--;
                dayData[dataTime][0] += record.getWorkingHours();
            }
        }

        for (int i = 0; i < dayData.length; i++) {
            for (int k = 0; k < dayData[i].length; k++) {
                double data = dayData[i][k];
                Map<String, String> mapMap = new HashMap<>();
                mapMap.put("day", "第" + (i + 1) + "天");
                mapMap.put("workHours", data + "");
                resultList.add(mapMap);
            }
        }
        Result result = new Result();
        result.setCode(0);
        result.setData(resultList);
        result.setCount(resultList.size());
        return result;
    }


    @GetMapping("/getExamKnowledgeKpiList")
    @ResponseBody
    public Result getExamKpiList(HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();


        String startTime = request.getParameter("startTime");
        if (startTime != null) {
            map.put("createTime", startTime + "%");
        }
        String departmentId = request.getParameter("departmentId");
        if (departmentId != null && !"".equals(departmentId) && !"0".equals(departmentId)) {
            map.put("departmentId", departmentId);
        }

        Result result = new Result();
        Subject subject = SecurityUtils.getSubject();
        Users users = (Users) subject.getPrincipal();
        if (users == null) {
            result.setMsg("用户失效,请重新登录");
            return result;
        }
        Integer employeeId = users.getEmployeeId();
        boolean selectAllFlag = subject.isPermitted("员工信息查询所有");
        String empIdStr = "";
        if (!selectAllFlag) {
            List<String> employeeIdList = new ArrayList<>();

            List<Employee> rootList = employeeService.getEmployeeByManager(employeeId);

            List<Employee> empList = employeeService.getEmployeeByManager(0);
            ListUtils.getChildEmployeeId(rootList, empList, employeeIdList, null);

            map = new HashMap();
            for (String employeeIdStr : employeeIdList) {
                empIdStr += employeeIdStr + ",";
            }
            map.put("empId", empIdStr);
        }

        List<Map> list = examinationServive.getExamKpiList(map);

        result.setMsg("成功");
        result.setData(list);
        result.setCode(0);
        result.setCount(list.size());
        return result;

    }


}
