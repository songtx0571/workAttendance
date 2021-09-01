package com.howei.service;

import com.howei.pojo.*;

import java.util.List;
import java.util.Map;

public interface WorkingService {

    List<OperatingHours> getOperatingHoursList(Map map);

    /**
     * @param map
     * @return 检修记录
     */
    List<OverhaulRecord> getMaintainRecordByMap(Map<String, Object> map);

    List<OverhaulRecord> getDefectByMap(Map<String, Object> map);

    List<OverhaulRecord> getMaintenceRecordByMap(Map<String, Object> map);

    int insertManagerHours(ManagerHours managerHours);

    ManagerHours getManagerHoursByMap(Map<String, Object> map);

    int updateManagerHours(ManagerHours managerHours);

    List<ManagerHours> getManagerHoursListByMap(Map map);

    int insertWorkingHour(Map<String, Object> workingHour);

    int updateWorkingHourById(Map<String, Object> workingHour);

    List<Map<String, Object>> getWorkingHourByMap(Map<String, Object> map);

    int deleteWorkingHourByMap(Map<String, Object> map);
}
