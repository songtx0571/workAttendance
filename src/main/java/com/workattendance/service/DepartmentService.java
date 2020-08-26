package com.workattendance.service;

import com.workattendance.pojo.Department;

import java.util.List;

public interface DepartmentService {
    /**
     * 获取部门列表
     * @return
     */
    List<Department> getDepList();
}
