package com.workattendance.service;

import com.workattendance.mapper.Examinationmapper;
import com.workattendance.pojo.Examination;
import com.workattendance.pojo.MaintenanceRecord;
import com.workattendance.pojo.Salary;
import com.workattendance.pojo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ExaminationService extends CommonCrudservice {

    @Autowired
    Examinationmapper examinationmapper;

    @Autowired
    public ExaminationService(Examinationmapper examinationmapper) {
        super(examinationmapper);
        this.examinationmapper = examinationmapper;
    }

    public Examination getExamTimes(String userName, String cycle, Integer week, Integer questionId, Integer times) {
        return examinationmapper.getExamTimes(userName, cycle, week, questionId, times);
    }

    public Examination getWeek(Integer week) {
        return examinationmapper.getWeek(week);
    }

    public List<Users> getName(String userName) {
        return examinationmapper.getName(userName);
    }


    public List<Examination> findExamHis(String userName) {
        return examinationmapper.findExamHis(userName);
    }

    public List<MaintenanceRecord> showLaborAll(Map map) {
        return examinationmapper.showLaborAll(map);
    }

    public List<MaintenanceRecord> showLaborByMonth(Map map) {
        return examinationmapper.showLaborByMonth(map);
    }


    public List<MaintenanceRecord> showLaborByDay(Map map) {
        return examinationmapper.showLaborByDay(map);
    }

    public List<MaintenanceRecord> getWorkingHoursByProPeople(Map map) {
        return examinationmapper.getWorkingHoursByProPeople(map);
    }

    public List<Salary> getSalary1(String datetime) {
        return examinationmapper.getSalary1(datetime);
    }

    public List<Salary> getSalary2(String yearDate, String cycleMonth, String datetime) {
        return examinationmapper.getSalary2(yearDate, cycleMonth, datetime);
    }

    public List<Salary> getSalary1s(String datetime, String userName) {
        return examinationmapper.getSalary1s(datetime, userName);
    }

    public List<Salary> getSalary2s(String yearDate, String cycleMonth, String userName, String datetime) {
        return examinationmapper.getSalary2s(yearDate, cycleMonth, userName, datetime);
    }

    public List<MaintenanceRecord> getWorkingHoursByProPeople2(Integer projectId, String datetime, String name) {
        return examinationmapper.getWorkingHoursByProPeople2(projectId, datetime, name);
    }

    public List<MaintenanceRecord> getWorkingHoursByProPeople2D(Integer projectId, String datetime) {
        return examinationmapper.getWorkingHoursByProPeople2D(projectId, datetime);
    }

    public List<MaintenanceRecord> getMaintenanceByDate(Integer projectId, String datetime) {
        return examinationmapper.getMaintenanceByDate(projectId, datetime);
    }


    public void insertExamHis(Examination examination) {
        examinationmapper.insertExamHis(examination);
    }
}
