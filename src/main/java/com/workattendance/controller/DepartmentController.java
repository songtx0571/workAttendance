package com.workattendance.controller;

import com.workattendance.pojo.Department;
import com.workattendance.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wa/department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    /**
     * 查询部门列表
     * @return
     */
    @RequestMapping("/getDepartmentList")
    @ResponseBody
    public List<Map<String,Object>> getDepartmentList(){
        List<Map<String,Object>> list=new ArrayList<>();
        List<Department> departmentList=departmentService.getDepList();
        for(Department department:departmentList){
            Map<String,Object> map=new HashMap<>();
            map.put("id",department.getId());
            map.put("text",department.getDepartmentName());
            list.add(map);
        }
        return list;
    }
}
