package com.howei.service.impl;

import com.howei.pojo.Company;
import com.howei.pojo.Inform;
import com.howei.service.DepartmentService;
import com.howei.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    DepartmentMapper departmentMapper;

    @Override
    public List<Company> getDepList() {
        return departmentMapper.getDepList();
    }

    @Override
    public List<Map<String,String>> getDepartmentMap(Map map) {
        List<Company> list=departmentMapper.getDepartmentMap(map);
        List<Map<String,String>> result=new ArrayList<>();
        for (Company company:list){
            Map<String, String> map1=new HashMap<>();
            map1.put("id",company.getId()+"");
            map1.put("name",company.getName());
            result.add(map1);
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getCompanyMap() {
        List<Company> list=departmentMapper.getCompanyMap();
        List<Map<String,String>> result=new ArrayList<>();
        for (Company company:list){
            Map<String, String> map=new HashMap<>();
            map.put("id",company.getId()+"");
            map.put("name",company.getName());
            result.add(map);
        }
        return result;
    }
}
