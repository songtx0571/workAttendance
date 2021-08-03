package com.howei.service.impl;

import com.howei.mapper.WageBaseMapper;
import com.howei.pojo.PostGrade;
import com.howei.pojo.WagesPost;
import com.howei.service.WageBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WageBaseServiceImpl implements WageBaseService {

    @Autowired
    private WageBaseMapper wageBaseMapper;

    @Override
    public List<PostGrade> getPostGradeList(Map map) {
        return wageBaseMapper.getPostGradeList(map);
    }

    @Override
    public int findPostGradeByName(Integer postId, String gradeName) {
        return wageBaseMapper.findPostGradeByName(postId,gradeName);
    }

    @Override
    public int addPostGrade(PostGrade postGrade) {
        return wageBaseMapper.addPostGrade(postGrade);
    }

    @Override
    public PostGrade getPostBaseById(Integer id) {
        return wageBaseMapper.getPostBaseById(id);
    }

    @Override
    public int findPostGradeByMonth(Integer postId, String gradeName, String lastMonth) {
        return wageBaseMapper.findPostGradeByMonth(postId, gradeName, lastMonth);
    }

    @Override
    public int copyByMonth(PostGrade postGrade) {
        return wageBaseMapper.copyByMonth(postGrade);
    }

    @Override
    public List<WagesPost> getWagesPostList(Map map) {
        return wageBaseMapper.getWagesPostList(map);
    }

    @Override
    public WagesPost findWagesPostByName(String name) {
        return wageBaseMapper.findWagesPostByName(name);
    }

    @Override
    public int addWagesPost(WagesPost wagesPost) {
        return wageBaseMapper.addWagesPost(wagesPost);
    }

    @Override
    public int updWagesPost(WagesPost wagesPost) {
        return wageBaseMapper.updWagesPost(wagesPost);
    }

    @Override
    public int updPostGrade(PostGrade postGrade) {
        return wageBaseMapper.updPostGrade(postGrade);
    }

    @Override
    public List<Map<String, String>> getWagesPostMap() {
        List<Map<String, String>> result=new ArrayList<>();
        List<WagesPost> list=wageBaseMapper.getWagesPostMap();
        for(WagesPost wagesPost:list){
            Map<String,String> map=new HashMap<>();
            map.put("id",wagesPost.getId().toString());
            map.put("name",wagesPost.getName());
            map.put("wage",wagesPost.getWagesPostWage()+"");
            result.add(map);
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getPostGradeMap(Integer id) {
        List<Map<String, String>> result=new ArrayList<>();
        List<PostGrade> list=wageBaseMapper.getPostGradeMap(id);
        for(PostGrade postGrade:list){
            Map<String,String> map=new HashMap<>();
            map.put("id",postGrade.getId().toString());
            map.put("name",postGrade.getGradeName());
            map.put("wage",postGrade.getPostGradeWage()+"");
            result.add(map);
        }
        return result;
    }

    @Override
    public WagesPost getWagesPostById(Integer wagesPostId) {
        return wageBaseMapper.getWagesPostById(wagesPostId);
    }

}
