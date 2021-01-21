package com.howei.service;

import com.howei.pojo.Leave;
import com.howei.pojo.LeaveData;
import com.howei.pojo.Review;

import java.util.List;
import java.util.Map;

public interface LeaveService {
    List<Leave> getConfigureList(Map map);

    int addConfigure(Leave leave);

    Leave getLeave(String id);

    int updateConfigure(Leave leave);

    List<LeaveData> getLeaveDataList(Map map);

    List<Map> getLeaveNameMap();

    int addLeaveData(LeaveData leaveData);

    LeaveData getLeadData(String id);

    int updateLeaveData(LeaveData leaveData);

    List<Map> getConfigureMap();

    List<LeaveData> getLeaveDataStatisticsList(Map map);

    int addReview(Review reviewObject);

    List<LeaveData> getLeaveDataToAchievements(Map map);
}
