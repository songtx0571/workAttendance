package com.howei.service;

import com.howei.pojo.Assessment;
import com.howei.pojo.Performance;

import java.util.List;
import java.util.Map;

public interface PerformanceService {
    List<Performance> findAllAcc(Map map);

    int insert(Performance performance);

    List<String> selectCycle(int i);

    int update(Performance performance);

    Performance getPeAcc(String id);

    void deletePeAccById(String id);

    List<Performance> findPeAcc(Performance performance);
}
