package com.workattendance.service.impl;

import com.workattendance.mapper.DepartmentMapper;
import com.workattendance.pojo.Department;
import com.workattendance.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    DepartmentMapper departmentMapper;

    @Override
    public List<Department> getDepList() {
        return departmentMapper.getDepList();
    }
}
