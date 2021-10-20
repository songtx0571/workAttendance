package com.howei.mapper;

import com.howei.pojo.Leave;
import com.howei.pojo.LeaveData;
import com.howei.pojo.Review;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface LeaveMapper {
    List<Leave> getConfigureList(Map map);

    int addConfigure(Leave leave);

    Leave getLeave(@Param("id") String id);

    int updateConfigure(Leave leave);

    List<LeaveData> getLeaveDataList(Map map);

    int addLeaveData(LeaveData leaveData);

    LeaveData getLeadData(@Param("id")String id);

    int updateLeaveData(LeaveData leaveData);

    List<Leave> getConfigureMap();

    List<LeaveData> getLeaveDataStatisticsList(Map map);

    int addReview(Review reviewObject);

    List<LeaveData> getLeaveDataToAchievements(Map map);

    int deleteById(Integer id);
}
