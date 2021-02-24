package com.howei.service;

import com.howei.pojo.Company;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    /**
     * 获取部门列表
     * @return
     */
    List<Company> getDepList();

    List<Map<String,String>> getDepartmentMap(Map map);

    List<Map<String,String>> getCompanyMap();
}
