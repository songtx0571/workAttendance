package com.howei.mapper;

import com.howei.pojo.Users;
import com.howei.pojo.Examination;
import com.howei.pojo.MaintenanceRecord;
import com.howei.pojo.Salary;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public interface Examinationmapper extends Commonmapper {

    Examination getExamTimes(@Param("userName") String userName, @Param("cycle") String cycle, @Param("week") Integer week, @Param("questionId") Integer questionId, @Param("times") Integer times);

    Examination getWeek(@Param("week") Integer week);

    List<Users> getName(@Param("userName") String userName);

    void insertExamHis(@Param("examination") Examination examination);

    List<Examination> findExamHis(@Param("userName") String userName);

    List<MaintenanceRecord> showLaborAll(Map map);

    List<MaintenanceRecord> getWorkingHoursByProPeople(Map map);

    List<Salary> getSalary1(@Param("datetime") String datetime);

    List<Salary> getSalary2(@Param("yearDate") String yearDate, @Param("cycleMonth") String cycleMonth, @Param("datetime") String datetime);

    List<Salary> getSalary1s(@Param("datetime") String datetime, @Param("userName") String userName);

    List<Salary> getSalary2s(@Param("yearDate") String yearDate, @Param("cycleMonth") String cycleMonth, @Param("userName") String userName, @Param("datetime") String datetime);


//    List<Salary> getCoe (@Param("userName") String userName);

    List<MaintenanceRecord> getWorkingHoursByProPeople2(@Param("projectId") Integer projectId, @Param("datetime") String datetime, @Param("name") String name);

    List<MaintenanceRecord> getWorkingHoursByProPeople2D(@Param("projectId") Integer projectId, @Param("datetime") String datetime);

    List<MaintenanceRecord> getMaintenanceByDate(@Param("projectId") Integer projectId, @Param("datetime") String datetime);

    List<MaintenanceRecord> showLaborByDay(Map map);

    List<MaintenanceRecord> showLaborByMonth(Map map);

    List<Map> getExamKnowledgeKpiList(Map map);
}
