package com.howei.service;

import com.howei.pojo.Assessment;
import com.howei.pojo.Behavior;

import java.util.List;
import java.util.Map;

public interface BehaviorService {
    List<Assessment> getAssessment(Map map);

    List<String> selectBeCycle(String employeeId);

    int update(Behavior behavior);

    Behavior findAllBe(Behavior behavior);

    Assessment getAssessmentByEmployeeId(Map map);

    int insert(Behavior behavior);
}
