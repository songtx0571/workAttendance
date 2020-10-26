package com.howei.service.impl;

import com.howei.pojo.PostPerator;
import com.howei.service.PostPeratorService;
import com.howei.mapper.PostperatorMapper;
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
