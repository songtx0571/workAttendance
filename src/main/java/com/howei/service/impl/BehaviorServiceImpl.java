package com.howei.service.impl;

import com.howei.mapper.BehaviorMapper;
import com.howei.mapper.UsersMapper;
import com.howei.pojo.Achievement;
import com.howei.pojo.LeaveData;
import com.howei.service.BehaviorService;
import com.howei.pojo.Assessment;
import com.howei.pojo.Behavior;
import com.howei.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BehaviorServiceImpl implements BehaviorService {

    @Autowired
    BehaviorMapper behaviorMapper;
    @Autowired
    UsersMapper usersmapper;
    @Autowired
    private LeaveService leaveService;

    @Override
    public List<Assessment> getAssessment(Map map) {
        return behaviorMapper.getAssessment(map);
    }

    @Override
    public List<String> selectBeCycle(String employeeId) {
        return behaviorMapper.selectBeCycle(employeeId);
    }

    @Override
    public int update(Behavior behavior) {
        return behaviorMapper.update(behavior);
    }

    @Override
    public Behavior findAllBe(Behavior behaviorQO) {
        Behavior behavior = behaviorMapper.findAll(behaviorQO);
        if (behavior == null) {
            return null;
        }
        //获取考勤情况:请假记录
        Map map = new HashMap();
        map.put("employeeId", behavior.getEmployeeId());
        map.put("startTime", behavior.getCycle() + "-01");
        List<LeaveData> leaveDataList = leaveService.getLeaveDataToAchievements(map);
        if (leaveDataList != null && leaveDataList.size() > 0) {
            for (int i = 0; i < leaveDataList.size(); i++) {
                LeaveData leaveData = leaveDataList.get(i);
                Integer leaveCount = leaveData.getLeaveCount();
                Integer numValue = leaveData.getNumValue();
                double leaveResult = numValue * leaveCount;//考核分
                leaveData.setLeaveResult(leaveResult);
                int leaveUnit = leaveData.getLeaveUnit();//请假类型单位
                if (leaveUnit == 1) {
                    leaveData.setLeaveUnitName("天");
                } else if (leaveUnit == 2) {
                    leaveData.setLeaveUnitName("次");
                } else if (leaveUnit == 3) {
                    leaveData.setLeaveUnitName("月");
                } else if (leaveUnit == 4) {
                    leaveData.setLeaveUnitName("小时");
                }
            }
        }
        behavior.setLeaveData(leaveDataList);
        //获取工作业绩考勤
        List<Achievement> achievements = usersmapper.findAchievementsByUserName1(behaviorQO);
        for (Achievement achievement : achievements) {
            if (achievement.getWeek() == 1 && !"".equals(behavior.getWeek1())) {
                String week1 = String.valueOf(achievement.getMaxValue() / 10.0);
                behavior.setWeek1(week1);
            }
            if (achievement.getWeek() == 2 && !"".equals(behavior.getWeek2())) {
                String week2 = String.valueOf(achievement.getMaxValue() / 10.0);
                behavior.setWeek2(week2);
            }
            if (achievement.getWeek() == 3 && !"".equals(behavior.getWeek3())) {
                String week3 = String.valueOf(achievement.getMaxValue() / 10.0);
                behavior.setWeek3(week3);
            }
            if (achievement.getWeek() == 4 && !"".equals(behavior.getWeek4())) {
                String week4 = String.valueOf(achievement.getMaxValue() / 10.0);
                behavior.setWeek4(week4);
            }
        }
        return behavior;
    }

    @Override
    public Assessment getAssessmentByEmployeeId(Map map) {
        return behaviorMapper.getAssessmentByEmployeeId(map);
    }

    @Override
    public int insert(Behavior behavior) {
        return behaviorMapper.insert(behavior);
    }
}
