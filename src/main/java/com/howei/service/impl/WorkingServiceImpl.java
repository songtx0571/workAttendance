package com.howei.service.impl;

import com.howei.mapper.WorkingMapper;
import com.howei.pojo.*;
import com.howei.service.WorkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WorkingServiceImpl implements WorkingService {

    @Autowired
    private WorkingMapper workingMapper;

    @Override
    public List<OperatingHours> getOperatingHoursList(Map map) {
        return workingMapper.getOperatingHoursList(map);
    }

    @Override
    public List<OverhaulRecord> getMaintainRecordByMap(Map<String, Object> map) {
        return workingMapper.getMaintainRecordByMap(map);
    }

    @Override
    public List<OverhaulRecord> getDefectByMap(Map<String, Object> map) {
        return workingMapper.getDefectRecordByMap(map);

    }

    @Override
    public List<OverhaulRecord> getMaintenceRecordByMap(Map<String, Object> map) {
        return workingMapper.getMaintenceRecordByMap(map);
    }

    @Override
    public int insertManagerHours(ManagerHours managerHours) {
        return workingMapper.insertManagerHours(managerHours);
    }

    @Override
    public ManagerHours getManagerHoursByMap(Map<String, Object> map) {
        return workingMapper.getManagerHoursByMap(map);
    }

    @Override
    public int updateManagerHours(ManagerHours managerHours) {
        return workingMapper.updateManagerHours(managerHours);
    }

    @Override
    public List<ManagerHours> getManagerHoursListByMap(Map map) {
        return workingMapper.getManagerHoursListByMap(map);
    }

    @Override
    public int insertWorkingHour(Map<String, Object> workingHour) {
        return workingMapper.insertWorkingHour(workingHour);
    }

    @Override
    public int updateWorkingHourById(Map<String, Object> workingHour) {
        return workingMapper.updateWorkingHourById(workingHour);
    }

    @Override
    public List<Map<String, Object>> getWorkingHourByMap(Map<String, Object> map) {
        return workingMapper.getWorkingHourByMap(map);
    }

    @Override
    public int deleteWorkingHourByMap(Map<String, Object> map) {
        return workingMapper.deleteWrokingHourByMap(map);
    }

    @Override
    public int getMaxMinWorkAttendanceDayByMap(Map map) {
        List<Map<String, Object>> workingHourByMapList = workingMapper.getWorkingHourByMap(map);
        Integer WorkAttendance = 0;
        workingHourByMapList = workingHourByMapList.stream().filter(item -> item.containsKey("day")).collect(Collectors.toList());

        Object changedType = map.get("changedType");
        if ("0".equals(changedType)) {
            for (Map<String, Object> workingHourMap : workingHourByMapList) {
                for (int i = 1; i <= 31; i++) {
                    String key = (i <= 9) ? "0" + i : "" + i;
                    if (!"0".equals(workingHourMap.get(key)) && i < WorkAttendance) {
                        WorkAttendance = i;
                    }

                }
            }
        } else {
            for (Map<String, Object> workingHourMap : workingHourByMapList) {
                for (int i = 31; i > 1; i--) {
                    String key = (i <= 9) ? "0" + i : "" + i;
                    if (!"0".equals(workingHourMap.get(key)) && i > WorkAttendance) {
                        WorkAttendance = i;
                    }

                }
            }

        }

        return WorkAttendance;
    }
}

