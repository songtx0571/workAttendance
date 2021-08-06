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
    public Behavior findAllBe(Behavior behavior) {
        //获取学习能力:考试记录
        Behavior Behavior = behaviorMapper.findAll(behavior);
        if(Behavior==null){
            return null;
        }
        //获取考勤情况:请假记录
        Map map=new HashMap();
        System.out.println("behavior:::"+behavior);
        System.out.println(Behavior);
        map.put("employeeId",Behavior.getEmployeeId());
        map.put("startTime",Behavior.getCycle()+"-01");
        List<LeaveData> list=leaveService.getLeaveDataToAchievements(map);
        if(list!=null&&list.size()>0){
            for (int i=0;i<list.size();i++){
                LeaveData leaveData=list.get(i);
                Integer leaveCount=leaveData.getLeaveCount();
                Integer numValue=leaveData.getNumValue();
                double leaveResult=numValue*leaveCount;//考核分
                leaveData.setLeaveResult(leaveResult);
                int leaveUnit=leaveData.getLeaveUnit();//请假类型单位
                if(leaveUnit==1){
                    leaveData.setLeaveUnitName("天");
                }else if(leaveUnit==2){
                    leaveData.setLeaveUnitName("次");
                }else if(leaveUnit==3){
                    leaveData.setLeaveUnitName("月");
                }else if(leaveUnit==4){
                    leaveData.setLeaveUnitName("小时");
                }
            }
        }
        Behavior.setLeaveData(list);
        //获取工作业绩考勤
        List<Achievement> achievements = usersmapper.findAchievementsByUserName1(behavior);
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

    @Override
    public int insert(Behavior behavior) {
        return behaviorMapper.insert(behavior);
    }
}
