package com.howei.service.impl;

import com.howei.pojo.WorkPerator;
import com.howei.service.WorkPeratorService;
import com.howei.mapper.WorkPeratorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WorkPeratorServiceImpl implements WorkPeratorService {

    @Autowired
    WorkPeratorMapper workPeratorMapper;

    @Override
    public List<WorkPerator> selByUser(Map map) {
        return workPeratorMapper.selByUser(map);
    }

    @Override
    public String selByUserCount(Map map) {
        return workPeratorMapper.selByUserCount(map);
    }

    @Override
    public int addWorkPerator(WorkPerator workPerator) {
        return workPeratorMapper.addWorkPerator(workPerator);
    }

    @Override
    public int updStatus(Map<String, Object> map) {
        return workPeratorMapper.updStatus(map);
    }

    @Override
    public void delWorkPerator(Map map) {
        workPeratorMapper.delWorkPerator(map);
    }

    @Override
    public String selById(int id) {
        return workPeratorMapper.selById(id);
    }

    @Override
    public List<WorkPerator> getTemplateChildList(Map map) {
        return workPeratorMapper.getTemplateChildList(map);
    }

    @Override
    public WorkPerator selWorkperator(String id) {
        return workPeratorMapper.selWorkperator(id);
    }

    @Override
    public int updWorkperator(WorkPerator work) {
        return workPeratorMapper.updWorkperator(work);
    }

    @Override
    public int updWorkperatorChild(Map map) {
        return workPeratorMapper.updWorkperatorChild(map);
    }

    @Override
    public List<WorkPerator> selAll(Map map) {
        return workPeratorMapper.selAll(map);
    }

    @Override
    public List<String> selByParam(Map<String, Object> map) {
        return workPeratorMapper.selByParam(map);
    }

    @Override
    public int selAllCount(Map<String, Object> map) {
        return workPeratorMapper.selAllCount(map);
    }

    @Override
    public int getTemplateChildListCount(Map map) {
        return workPeratorMapper.getTemplateChildListCount(map);
    }

    @Override
    public List<Map> getTemplateMap(Map map) {
        return workPeratorMapper.getTemplateMap(map);
    }
}
