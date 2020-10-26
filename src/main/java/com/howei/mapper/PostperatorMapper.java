package com.howei.mapper;


import com.howei.pojo.PostPerator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public interface PostperatorMapper {

    List<Map> getKPIList(Map map);

    List<PostPerator> getFrequencyList(Map map);
}
