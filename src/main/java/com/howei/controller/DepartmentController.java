package com.howei.controller;

import com.howei.pojo.Company;
import com.howei.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wa/department")
@CrossOrigin(origins="http://test.hopeop.com:80",allowCredentials = "true")
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
        List<Company> departmentList=departmentService.getDepList();
        for(Company company:departmentList){
            Map<String,Object> map=new HashMap<>();
            map.put("id",company.getId());
            map.put("text",company.getName());
            list.add(map);
        }
        return list;
    }
}
