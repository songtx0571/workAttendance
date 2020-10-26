package com.howei.mapper;

import com.howei.pojo.Inform;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface InformMapper {
    List<Map> getInformKPIList(Map map);

    List<Inform> getSelList(Map map);

    List<Inform> getCreatedList(Map map);
}
