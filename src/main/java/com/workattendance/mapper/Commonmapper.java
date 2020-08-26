package com.workattendance.mapper;

import com.workattendance.pojo.Examination;
import com.workattendance.pojo.ProjectTeam;
import com.workattendance.pojo.Salary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Commonmapper {
    void insert(@Param("obj") Object obj);

    void delete(@Param("obj") Object obj);

    void update(@Param("obj") Object obj);

    void updateSalary(@Param("obj") Object obj);

    void updateSalaryCopy(@Param("obj") Object obj);


    Object findById(@Param("obj") Object obj);

    List<Object> findAll();

    List<Object> findAllByM(@Param("manager") String manager);

    List<ProjectTeam> find();

    List<Salary> getCoe();

    List<Examination> getExamTimes();

    int count();
}
