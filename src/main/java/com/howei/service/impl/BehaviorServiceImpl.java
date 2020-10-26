package com.howei.service.impl;

import com.howei.mapper.BehaviorMapper;
import com.howei.mapper.UsersMapper;
import com.howei.pojo.Achievement;
import com.howei.service.BehaviorService;
import com.howei.pojo.Assessment;
import com.howei.pojo.Behavior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BehaviorServiceImpl implements BehaviorService {

    @Autowired
    BehaviorMapper behaviorMapper;
    @Autowired
    UsersMapper usersmapper;

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
    public Behavior findAllBe(Behavior behavior) {
        Behavior Behavior = behaviorMapper.findAll(behavior);
        List<Achievement> achievements = usersmapper.findAchievementsByUserName1(behavior);
        System.out.println(achievements.size());
        for (Achievement achievement : achievements) {
            if (achievement.getWeek() == 1 && Behavior.getWeek1() != "") {
                Behavior.setWeek1("" + ((double) achievement.getMaxValue()) / 10);
            }
            if (achievement.getWeek() == 2 && Behavior.getWeek2() != "") {
                Behavior.setWeek2("" + ((double) achievement.getMaxValue()) / 10);
            }
            if (achievement.getWeek() == 3 && Behavior.getWeek3() != "") {
                Behavior.setWeek3("" + ((double) achievement.getMaxValue()) / 10);
            }
            if (achievement.getWeek() == 4 && Behavior.getWeek4() != "") {
                Behavior.setWeek4("" + ((double) achievement.getMaxValue()) / 10);
            }
        }

        return Behavior;
    }

    @Override
    public Assessment getAssessmentByEmployeeId(Map map) {
        return behaviorMapper.getAssessmentByEmployeeId(map);
    }
}
