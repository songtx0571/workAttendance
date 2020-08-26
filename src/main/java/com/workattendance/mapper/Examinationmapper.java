package com.workattendance.mapper;

import com.workattendance.pojo.Examination;
import com.workattendance.pojo.MaintenanceRecord;
import com.workattendance.pojo.Salary;
import com.workattendance.pojo.Users;
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
}
