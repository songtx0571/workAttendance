package com.howei.mapper;

import com.howei.pojo.Company;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DepartmentMapper {
    /**
     * 获取部门列表
     * @return
     */
    List<Company> getDepList();
}
