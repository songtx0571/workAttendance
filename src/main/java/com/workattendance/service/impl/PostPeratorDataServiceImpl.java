package com.workattendance.service.impl;

import com.workattendance.mapper.PostperatorDataMapper;
import com.workattendance.pojo.PostPeratorData;
import com.workattendance.service.PostPeratorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class PostPeratorDataServiceImpl implements PostPeratorDataService {

    @Autowired
    PostperatorDataMapper postperatorDataMapper;

    @Override
    public int crePostChild(PostPeratorData postPeratorData) {
        return postperatorDataMapper.crePostChild(postPeratorData);
    }

    @Override
    public List<PostPeratorData> selByEquipment(Map map) {
        return postperatorDataMapper.selByEquipment(map);
    }

    @Override
    public int updPostData(Map map) {
        return postperatorDataMapper.updPostData(map);
    }

    @Override
    public List<Map> selTypeByName(Map map) {
        return postperatorDataMapper.selTypeByName(map);
    }

    @Override
    public List<PostPeratorData> selByName(Map m) {
        return postperatorDataMapper.selByName(m);
    }

    @Override
    public List<PostPeratorData> selPostPerDataById(String postPeratorId) {
        return postperatorDataMapper.selPostPerDataById(postPeratorId);
    }

    @Override
    public List<PostPeratorData> getPointList(Map map) {
        return postperatorDataMapper.getPointList(map);
    }
}
