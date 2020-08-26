package com.workattendance.mapper;

import com.workattendance.pojo.Department;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DepartmentMapper {
    /**
     * 获取部门列表
     * @return
     */
    List<Department> getDepList();
}
