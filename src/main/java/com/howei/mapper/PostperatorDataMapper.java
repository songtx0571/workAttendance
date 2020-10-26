package com.howei.mapper;

import com.howei.pojo.PostPeratorData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public interface PostperatorDataMapper {

    int crePostChild(PostPeratorData postPeratorData);

    List<PostPeratorData> selByEquipment(Map map);

    int updPostData(Map map);

    List<Map> selTypeByName(Map map);

    List<PostPeratorData> selByName(Map m);

    List<PostPeratorData> selPostPerDataById(String postPeratorId);

    List<PostPeratorData> getPointList(Map map);
}
