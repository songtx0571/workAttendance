package com.howei.service;

import com.howei.pojo.Company;

import java.util.List;

public interface DepartmentService {
    /**
     * 获取部门列表
     * @return
     */
    List<Company> getDepList();
}
