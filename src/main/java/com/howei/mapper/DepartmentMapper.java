package com.howei.mapper;

import com.howei.pojo.Company;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface DepartmentMapper {
    /**
     * 获取部门列表
     * @return
     */
    List<Company> getDepList();

    List<Company> getDepartmentMap(Map map);

    List<Company> getCompanyMap();

    List<Map<String, String>> getDepMap();
}
