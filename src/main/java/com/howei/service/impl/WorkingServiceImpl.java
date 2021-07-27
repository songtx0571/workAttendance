package com.howei.service.impl;

import com.howei.mapper.WorkingMapper;
import com.howei.pojo.*;
import com.howei.service.WorkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

}
