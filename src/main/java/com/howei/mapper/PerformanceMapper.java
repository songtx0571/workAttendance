package com.howei.mapper;

import com.howei.pojo.Assessment;
import com.howei.pojo.Performance;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface PerformanceMapper {
    List<Performance> findAllAcc(Map map);

    int insert(Performance performance);

    List<String> selectCycle(@Param("employeeId") int employeeId);

    int update(Performance performance);

    Performance getPeAcc(@Param("id") String id);

    void deletePeAccById(@Param("id") String id);

    List<Performance> findPeAcc(Performance performance);
}
