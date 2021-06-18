package com.howei.mapper;

import com.howei.pojo.OperatingHours;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface WorkingMapper {

    List<OperatingHours> getOperatingHoursList(Map map);
}
