package com.howei.service;

import com.howei.pojo.PostGrade;
import com.howei.pojo.WagesPost;

import java.util.List;
import java.util.Map;

public interface WageBaseService {

    List<PostGrade> getPostGradeList(Map map);

    int findPostGradeByName(Integer postId, String gradeName);

    int addPostGrade(PostGrade postGrade);

    PostGrade getPostBaseById(Integer id);

    /**
     * 查询指定月份是否存在岗位工资信息
     * @param postId 岗位Id
     * @param gradeName 等级Id
     * @param lastMonth 指定月份
     * @return
     */
    int findPostGradeByMonth(Integer postId, String gradeName, String lastMonth);

    int copyByMonth(PostGrade postGrade);

    List<WagesPost> getWagesPostList(Map map);

    WagesPost findWagesPostByName(String name);

    int addWagesPost(WagesPost wagesPost);

    int updWagesPost(WagesPost wagesPost);

    int updPostGrade(PostGrade postGrade);

    List<Map<String,String>> getWagesPostMap();

    List<Map<String,String>> getPostGradeMap(Integer id);

    WagesPost getWagesPostById(Integer wagesPostId);
}
