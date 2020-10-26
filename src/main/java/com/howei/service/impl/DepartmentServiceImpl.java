package com.howei.service.impl;

import com.howei.pojo.Company;
import com.howei.service.DepartmentService;
import com.howei.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    DepartmentMapper departmentMapper;

    @Override
    public List<Company> getDepList() {
        return departmentMapper.getDepList();
    }
}
