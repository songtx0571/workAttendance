package com.workattendance.service;

import com.workattendance.pojo.PostPeratorData;

import java.util.List;
import java.util.Map;

public interface PostPeratorDataService {


    int crePostChild(PostPeratorData postPeratorData);

    /**
     * 根据设备名称查询
     * @param map
     * @return
     */
    List<PostPeratorData> selByEquipment(Map map);

    int updPostData(Map map);

    List<Map> selTypeByName(Map map);

    List<PostPeratorData> selByName(Map m);

    List<PostPeratorData> selPostPerDataById(String postPeratorId);

    List<PostPeratorData> getPointList(Map map);
}
