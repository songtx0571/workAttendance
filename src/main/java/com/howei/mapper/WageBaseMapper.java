package com.howei.mapper;

import com.howei.pojo.PostGrade;
import com.howei.pojo.WagesPost;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface WageBaseMapper {

    List<PostGrade> getPostGradeList(Map map);

    int findPostGradeByName(@Param("wagesPostId") Integer wagesPostId,@Param("gradeName") String gradeName);

    int addPostGrade(PostGrade postGrade);

    PostGrade getPostBaseById(Integer id);

    int findPostGradeByMonth(Integer postId, String gradeName, String lastMonth);

    int copyByMonth(PostGrade postGrade);

    List<WagesPost> getWagesPostList(Map map);

    WagesPost findWagesPostByName(String name);

    int addWagesPost(WagesPost wagesPost);

    int updWagesPost(WagesPost wagesPost);

    int updPostGrade(PostGrade postGrade);

    List<WagesPost> getWagesPostMap();

    List<PostGrade> getPostGradeMap(@Param("wagesPostId") int wagesPostId);

    WagesPost getWagesPostById(Integer wagesPostId);
}
