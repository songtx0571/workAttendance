package com.howei.controller;

import com.alibaba.fastjson.JSON;
import com.howei.pojo.Employee;
import com.howei.pojo.Users;
import com.howei.service.EmployeeService;
import com.howei.util.Page;
import com.howei.util.Result;
import com.howei.util.Type;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.shiro.authz.annotation.Logical.OR;

@Controller
@RequestMapping("/wa/employee")
//@CrossOrigin(origins="http://test.hopeop.com:80",allowCredentials = "true")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/toEmployee")
    public String toEmployee(){
        return "employee";
    }

    public Users getPrincipal(){
        Subject subject=SecurityUtils.getSubject();
        Users users=(Users)subject.getPrincipal();
        return users;
    }

    /**
     * 获取员工登录信息
     * @return
     */
    @RequestMapping("/getEmployeeInf")
    @ResponseBody
    public String getEmployeeInf(){
        Subject subject=SecurityUtils.getSubject();
        Users users=(Users)subject.getPrincipal();
        Employee employee=employeeService.getEmployeeById(users.getEmployeeId()+"");
        return JSON.toJSONString(employee);
    }

    /**
     * 获取员工列表
     * @return result
     */
    @RequiresPermissions(value = {"员工信息"}, logical = OR)
    @RequestMapping("/getEmployeeList")
    @ResponseBody
    public String getEmployeeList(String sign) {
        Subject subject = SecurityUtils.getSubject();
        Users users=(Users)subject.getPrincipal();
        boolean selectAllFlag = subject.isPermitted("员工信息查询所有");
        List<Employee> list = null;
        Integer employeeId = null;
        String empIdStr = "";
        if (users != null) {
            employeeId = users.getEmployeeId();
        }
        if (selectAllFlag) {
            Map map = new HashMap();
            if(sign!=null && !sign.equals("")){
                map.put("sign", sign);
            }
            list = employeeService.getEmployeeList(map);
        } else {
            List<Employee> rootList = employeeService.getEmployeeByManager(employeeId);
            if (rootList != null) {
                empIdStr += employeeId + ",";
                List<Employee> empList = employeeService.getEmployeeByManager(0);
                for (Employee employee : rootList) {
                    empIdStr += employee.getId() + ",";
                    empIdStr += getUsersId(employee.getId(), empList);
                }
            }
            if (empIdStr != null && !empIdStr.equals("")) {
                empIdStr = empIdStr.substring(0, empIdStr.lastIndexOf(","));
            }
            Map map = new HashMap();
            map.put("empId", empIdStr);
            if(sign!=null && !sign.equals("")){
                map.put("sign", sign);
            }
            list = employeeService.getEmployeeList(map);
        }
        Result result = new Result();
        result.setCode(0);
        result.setCount(list.size());
        result.setData(list);
        return JSON.toJSONString(result);
    }

    public String getUsersId(Integer empId,List<Employee> empList){
        List<String> result=new ArrayList<>();
        String userId="";
        String usersId="";
        for(Employee employee:empList){
            if(employee.getManager()!=null||employee.getManager()!=0){
                if(employee.getManager().equals(empId)){
                    usersId+=employee.getId()+",";
                    result.add(employee.getId()+"");
                }
            }
        }
        for(String str:result){
            String userId1=getUsersId(Integer.parseInt(str),empList);
            if(userId1!=null&&!userId1.equals("")){
                userId+=userId1;
            }
        }
        if(userId!=null&&!userId.equals("null")){
            usersId+=userId;
        }
        return usersId;
    }

    /**
     * 下拉框员工
     * @return
     */
    @RequestMapping("/getEmployeeNameMap")
    @ResponseBody
    public String getEmployeeName(){
        List<Map> list=employeeService.getEmployeeNameMap(new HashMap());
        return JSON.toJSONString(list);
    }

    /**
     * 获取员工信息
     * @param request
     * @return
     */
    @RequestMapping("/getEmployee")
    @ResponseBody
    public String getEmployee(HttpServletRequest request){
        String id=request.getParameter("id");
        Employee employee=new Employee();
        if(id!=null&&!id.equals("")){
            employee=employeeService.getEmployeeById(id);
        }
        return JSON.toJSONString(employee);
    }

    /**
     * 修改
     * @param employee
     * @return
     */
    @RequestMapping(value="/updateEmployee",method = RequestMethod.POST)
    @ResponseBody
    public String updateEmployee(@RequestBody Employee employee){
        if(employee!=null&&employee.getId()!=-1){
            employeeService.updateEmployee(employee);
            return JSON.toJSONString("success");
        }
        return JSON.toJSONString("");
    }

    @RequiresPermissions(value = {"员工信息"}, logical = OR)
    @RequestMapping("/searchEmployee")
    @ResponseBody
    public String searchEmployee(HttpServletRequest request) {
        Users users = this.getPrincipal();
        Integer employeeId = null;
        String empIdStr = "";
        if (users != null) {
            employeeId = users.getEmployeeId();
        }
        List<Employee> rootList = employeeService.getEmployeeByManager(employeeId);
        if (rootList != null) {
            empIdStr += employeeId + ",";
            List<Employee> empList = employeeService.getEmployeeByManager(0);
            for (Employee employee : rootList) {
                empIdStr += employee.getId() + ",";
                empIdStr += getUsersId(employee.getId(), empList);
            }
        }
        if (empIdStr != null && !empIdStr.equals("")) {
            empIdStr = empIdStr.substring(0, empIdStr.lastIndexOf(","));
        }
        String search = request.getParameter("search");
        Map map = new HashMap();
        map.put("empId", empIdStr);
        map.put("search", search);
        List<Employee> list = employeeService.searchEmployee(map);
        Result result = new Result();
        result.setCode(0);
        result.setCount(list.size());
        result.setData(list);
        return JSON.toJSONString(result);
    }

    /**-----------------------------------------------获取未分配人员-------------------------------------------*/

    /**
     * 获取未分配人员列表
     * @return
     */
    @RequestMapping("getNoDistributionList")
    @ResponseBody
    public String getNoDistributionList(){
        Subject subject = SecurityUtils.getSubject();
        boolean selectAllFlag = subject.isPermitted("员工信息查询所有");
        Users users=this.getPrincipal();
        //用户信息过期，重新登录
        if (users == null) {
            return JSON.toJSONString(Type.noUser);
        }
        //没有查询权限
        if (!selectAllFlag) {
            return JSON.toJSONString(null);
        }
        Map map = new HashMap();
        map.put("sign", "noDistribution");
        List<Employee> list = employeeService.getEmployeeList(map);
        if(list==null){
            return JSON.toJSONString(0);
        }
        return JSON.toJSONString(list.size());
    }

}
