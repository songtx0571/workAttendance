package com.howei.service.impl;

import com.howei.mapper.PerformanceMapper;
import com.howei.pojo.Assessment;
import com.howei.pojo.Performance;
import com.howei.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PerformanceServiceImpl implements PerformanceService {

    @Autowired
    PerformanceMapper performanceMapper;

    @Override
    public List<Assessment> findAllAcc(Map map) {
        return performanceMapper.findAllAcc(map);
    }

    @Override
    public int insert(Performance performance) {
        return performanceMapper.insert(performance);
    }

    @Override
    public List<String> selectCycle(int employeeId) {
        return performanceMapper.selectCycle(employeeId);
    }

    @Override
    public int update(Performance performance) {
        return performanceMapper.update(performance);
    }

    @Override
    public Performance getPeAcc(String id) {
        return performanceMapper.getPeAcc(id);
    }

    @Override
    public void deletePeAccById(String id) {
        performanceMapper.deletePeAccById(id);
    }

    @Override
    public List<Performance> findPeAcc(Performance performance) {
        return performanceMapper.findPeAcc(performance);
    }
}
