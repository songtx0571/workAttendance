package com.workattendance.service;

import com.workattendance.pojo.PostPerator;

import java.util.List;
import java.util.Map;

public interface PostPeratorService {

    List<Map> getKPIList(Map map);

    List<PostPerator> getFrequencyList(Map map);

}
