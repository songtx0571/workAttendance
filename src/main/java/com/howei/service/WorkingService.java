package com.howei.service;

import com.howei.pojo.*;

import java.util.List;
import java.util.Map;

public interface WorkingService {

    List<OperatingHours> getOperatingHoursList(Map map);

    /**
     *
     * @param map
     * @return 检修记录
     */
    List<OverhaulRecord> getMaintainRecordByMap(Map<String, Object> map);

    List<OverhaulRecord> getDefectByMap(Map<String, Object> map);

    List<OverhaulRecord> getMaintenceRecordByMap(Map<String, Object> map);
}
