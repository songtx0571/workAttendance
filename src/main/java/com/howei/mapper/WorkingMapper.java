package com.howei.mapper;

import com.howei.pojo.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface WorkingMapper {

    List<OperatingHours> getOperatingHoursList(Map map);

    List<OverhaulRecord> getMaintainRecordByMap(Map<String, Object> map);

    List<OverhaulRecord> getDefectRecordByMap(Map<String, Object> map);

    List<OverhaulRecord> getMaintenceRecordByMap(Map<String, Object> map);
}
