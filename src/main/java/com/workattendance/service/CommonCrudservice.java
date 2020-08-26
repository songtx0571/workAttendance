package com.workattendance.service;

import com.github.pagehelper.PageHelper;
import com.workattendance.mapper.Commonmapper;
import com.workattendance.pojo.Examination;
import com.workattendance.pojo.PageBean;
import com.workattendance.pojo.ProjectTeam;

import java.util.List;


public abstract class CommonCrudservice {
    private Commonmapper mapper;

    public CommonCrudservice(Commonmapper mapper) {
        this.mapper = mapper;
    }

    public void insert(Object obj) {
        mapper.insert(obj);
    }

    public void delete(Object obj) {
        mapper.delete(obj);
    }

    public void update(Object obj) {
        mapper.update(obj);
    }

    public void updateSalary(Object obj) {
        mapper.updateSalary(obj);
    }

    public void updateSalaryCopy(Object obj) {
        mapper.updateSalaryCopy(obj);
    }


    public Object findById(Integer id) {
        return mapper.findById(id);
    }

    public List<Object> findAll() {
        return mapper.findAll();
    }

    public List<ProjectTeam> find() {
        return mapper.find();
    }

    public List<Examination> getExamTimes() {
        return mapper.getExamTimes();
    }


    public List<Object> findByPage(int currentPage, int pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<Object> allItems = (List) mapper.findAll();        //全部商品
        int countNums = mapper.count();            //总记录数
        PageBean<Object> pageData = new PageBean<>(currentPage, pageSize, countNums);
        pageData.setItems(allItems);
        return pageData.getItems();
    }

    public List<Object> findByPage2(int currentPage, int pageSize, String manager) {
        PageHelper.startPage(currentPage, pageSize, manager);
        List<Object> allItems = (List) mapper.findAllByM(manager);        //全部商品
        int countNums = mapper.count();            //总记录数
        PageBean<Object> pageData = new PageBean<>(currentPage, pageSize, countNums);
        pageData.setItems(allItems);
        return pageData.getItems();
    }

}
