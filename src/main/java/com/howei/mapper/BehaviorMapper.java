package com.howei.mapper;

import com.howei.pojo.Assessment;
import com.howei.pojo.Behavior;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface BehaviorMapper {

    List<Assessment> getAssessment(Map map);

    List<String> selectBeCycle(@Param("employeeId") String employeeId);

    int update(Behavior behavior);

    Behavior findAll(@Param("behavior") Behavior behavior);

    Assessment getAssessmentByEmployeeId(Map map);
}
