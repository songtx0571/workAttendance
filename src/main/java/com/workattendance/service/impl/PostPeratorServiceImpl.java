package com.workattendance.service.impl;

import com.workattendance.mapper.PostperatorMapper;
import com.workattendance.pojo.PostPerator;
import com.workattendance.service.PostPeratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PostPeratorServiceImpl implements PostPeratorService {

    @Autowired
    PostperatorMapper postperatorMapper;

    @Override
    public List<Map> getKPIList(Map map) {
        return postperatorMapper.getKPIList(map);
    }

    @Override
    public List<PostPerator> getFrequencyList(Map map) {
        return postperatorMapper.getFrequencyList(map);
    }

}
