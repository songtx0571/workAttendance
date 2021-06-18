package com.howei.service.impl;

import com.howei.mapper.WorkingMapper;
import com.howei.pojo.OperatingHours;
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
}
