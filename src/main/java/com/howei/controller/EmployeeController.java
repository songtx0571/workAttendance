package com.howei.controller;

import com.alibaba.fastjson.JSON;
import com.howei.pojo.Employee;
import com.howei.pojo.Users;
import com.howei.service.EmployeeService;
import com.howei.util.*;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.shiro.authz.annotation.Logical.OR;

@Controller
@RequestMapping("/wa/employee")
//@CrossOrigin(origins="http://test.hopeop.com:80",allowCredentials = "true")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/toEmployee")
    public String toEmployee() {
        return "employee";
    }

    public Users getPrincipal() {
        Subject subject = SecurityUtils.getSubject();
        Users users = (Users) subject.getPrincipal();
        return users;
    }

    /**
     * 获取员工登录信息
     *
     * @return
     */
    @RequestMapping("/getEmployeeInf")
    @ResponseBody
    public Result getEmployeeInf() {
        Subject subject = SecurityUtils.getSubject();
        Users users = (Users) subject.getPrincipal();
        if (users == null) {
            return Result.fail(ResultEnum.NO_USER);
        }
        int employeeId = users.getEmployeeId();
        Employee employee = employeeService.getEmployeeById(String.valueOf(employeeId));

        return Result.ok(1, employee);
    }

    /**
     * 查询员工信息
     *
     * @param sign noDistribution未分配,distributiond已分配
     * @return
     */
    @RequiresPermissions(value = {"员工信息"}, logical = OR)
    @RequestMapping("/getEmployeeList")
    @ResponseBody
    public Result getEmployeeList(String sign) {
        Subject subject = SecurityUtils.getSubject();
        Users users = (Users) subject.getPrincipal();
        if (users == null) {
            return Result.fail(ResultEnum.NO_USER);
        }
        boolean selectAllFlag = subject.isPermitted("员工信息查询所有");
        List<Employee> list = null;
        Integer employeeId = users.getEmployeeId();
        Map map = new HashMap();
        if (selectAllFlag) {
            if (sign != null && !"".equals(sign)) {
                map.put("sign", sign);
            }
            list = employeeService.getEmployeeList(map);
        } else {
            List<String> employeeIdList = new ArrayList<>();
            employeeIdList.add(employeeId.toString());
            List<Employee> rootList = employeeService.getEmployeeByManager(employeeId);
            List<Employee> empList = employeeService.getEmployeeByManager(0);
            ListUtils.getChildEmployeeId(rootList, empList, employeeIdList, null);
            String empIdStr = employeeIdList.stream().collect(Collectors.joining(","));
            map.put("empId", empIdStr);
            if (sign != null && !"".equals(sign)) {
                map.put("sign", sign);
            }
            list = employeeService.getEmployeeList(map);
        }
        return Result.ok(list.size(), list);
    }


    /**
     * 下拉框员工
     *
     * @return
     */
    @RequestMapping("/getEmployeeNameMap")
    @ResponseBody
    public Result getEmployeeName() {
        Subject subject = SecurityUtils.getSubject();
        Users users = (Users) subject.getPrincipal();
        if (users == null) {
            return Result.fail(ResultEnum.NO_USER);
        }
        List<Map> list = employeeService.getEmployeeNameMap(null);
        return Result.ok(list.size(), list);
    }

    /**
     * 获取员工信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/getEmployee")
    @ResponseBody
    public Result getEmployee(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        Users users = (Users) subject.getPrincipal();
        if (users == null) {
            return Result.fail(ResultEnum.NO_USER);
        }
        String id = request.getParameter("id");
        if (StringUtils.isEmpty(id)) {
            return Result.fail(ResultEnum.NO_PARAMETERS);
        }
        Employee employee = employeeService.getEmployeeById(id);
        return Result.ok(1, employee);
    }

    /**
     * 修改
     *
     * @param employee
     * @return
     */
    @RequestMapping(value = "/updateEmployee", method = RequestMethod.POST)
    @ResponseBody
    public Result updateEmployee(@RequestBody Employee employee) {
        Subject subject = SecurityUtils.getSubject();
        Users users = (Users) subject.getPrincipal();
        if (users == null) {
            return Result.fail(ResultEnum.NO_USER);
        }
        if (employee.getId() != null) {
            employeeService.updateEmployee(employee);
        }
        return Result.ok();
    }

    @RequiresPermissions(value = {"员工信息"}, logical = OR)
    @RequestMapping("/searchEmployee")
    @ResponseBody
    public Result searchEmployee(HttpServletRequest request) {
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
        Map map = new HashMap();
        String empIdStr = employeeIdList.stream().collect(Collectors.joining(","));
        map.put("empId", empIdStr);
        String search = request.getParameter("search");
        if (search != null && !"".equals(search)) {
            map.put("search", search);
        }
        List<Employee> list = employeeService.searchEmployee(map);
        return Result.ok(list.size(), list);
    }

    /**-----------------------------------------------获取未分配人员-------------------------------------------*/

    /**
     * 获取未分配人员列表
     *
     * @return
     */
    @RequestMapping("getNoDistributionList")
    @ResponseBody
    public Result getNoDistributionList() {
        Subject subject = SecurityUtils.getSubject();
        boolean selectAllFlag = subject.isPermitted("员工信息查询所有");
        Users users = this.getPrincipal();
        //用户信息过期，重新登录
        if (users == null) {
            return Result.fail(ResultEnum.NO_USER);
        }
        //没有查询权限
        if (!selectAllFlag) {
            return Result.fail(ResultEnum.NO_PERMISSION);
        }
        Map map = new HashMap();
        map.put("sign", "noDistribution");
        List<Employee> list = employeeService.getEmployeeList(map);
        if (list != null && list.size() > 0) {
            return Result.ok(list.size(), list.size());
        }
        return Result.ok(0, 0);
    }

}
