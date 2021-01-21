package com.howei.service.impl;

import com.howei.pojo.Leave;
import com.howei.pojo.LeaveData;
import com.howei.pojo.Review;
import com.howei.service.LeaveService;
import com.howei.mapper.LeaveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private LeaveMapper leaveMapper;

    @Override
    public List<Leave> getConfigureList(Map map) {
        return leaveMapper.getConfigureList(map);
    }

    @Override
    public int addConfigure(Leave leave) {
        return leaveMapper.addConfigure(leave);
    }

    @Override
    public Leave getLeave(String id) {
        return leaveMapper.getLeave(id);
    }

    @Override
    public int updateConfigure(Leave leave) {
        return leaveMapper.updateConfigure(leave);
    }

    @Override
    public List<LeaveData> getLeaveDataList(Map map) {
        List<LeaveData> list=leaveMapper.getLeaveDataList(map);
        if(list!=null&&list.size()>0){
            for (LeaveData leaveData:list){
                Integer status=leaveData.getStatus();
                if(status==0){
                    leaveData.setStatusName("未审核");
                }else if(status==1){
                    leaveData.setStatusName("审核中");
                }else if(status==2){
                    leaveData.setStatusName("审核完毕");
                }
            }
        }
        return list;
    }

    @Override
    public List<Map> getLeaveNameMap() {
        List<Map> result=new ArrayList<>();
        List<Leave> list=leaveMapper.getConfigureList(new HashMap());
        if(list!=null){
            for (Leave leave:list){
                Map map=new HashMap();
                map.put("id",leave.getId());
                map.put("name",leave.getName());
                result.add(map);
            }
        }
        return result;
    }

    @Override
    public int addLeaveData(LeaveData leaveData) {
        return leaveMapper.addLeaveData(leaveData);
    }

    @Override
    public LeaveData getLeadData(String id) {
        return leaveMapper.getLeadData(id);
    }

    @Override
    public int updateLeaveData(LeaveData leaveData) {
        return leaveMapper.updateLeaveData(leaveData);
    }

    @Override
    public List<Map> getConfigureMap() {
        List<Map> result=new ArrayList<>();
        List<Leave> list=leaveMapper.getConfigureMap();
        if(list!=null){
            for (Leave leave:list){
                Map map=new HashMap();
                map.put("id",leave.getId());
                map.put("name",leave.getName());
                result.add(map);
            }
        }
        return result;
    }

    @Override
    public List<LeaveData> getLeaveDataStatisticsList(Map map) {
        List<LeaveData> list=leaveMapper.getLeaveDataStatisticsList(map);
        if(list!=null && list.size()>0){
            for(LeaveData leaveData:list){

            }
        }
        return list;
    }

    @Override
    public int addReview(Review reviewObject) {
        return leaveMapper.addReview(reviewObject);
    }

    @Override
    public List<LeaveData> getLeaveDataToAchievements(Map map) {
        return leaveMapper.getLeaveDataToAchievements(map);
    }
}
