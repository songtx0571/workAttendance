package com.workattendance.mapper;


import com.workattendance.pojo.PostPerator;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public interface PostperatorMapper {

    List<Map> getKPIList(Map map);

    List<PostPerator> getFrequencyList(Map map);
}
