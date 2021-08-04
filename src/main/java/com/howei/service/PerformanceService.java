package com.howei.service;

import com.howei.pojo.Performance;

import java.util.List;
import java.util.Map;

public interface PerformanceService {
    List<Performance> findAllAcc(Map map);

    int insert(Performance performance);

    List<String> selectCycle(int i);

    int update(Performance performance);

    Performance getPeAcc(String id);

    /**
     * 逻辑删除
     *
     * @param id 业绩考核项id
     */
    void deletePeAccById(String id);

    List<Performance> findPeAcc(Performance performance);

    /**
     * 批量修改isActive为0
     * @param ids
     * @param isActive
     * @return
     */
    int updateIsActiveByIds(String ids, boolean isActive);
}
