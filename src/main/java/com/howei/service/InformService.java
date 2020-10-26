package com.howei.service;

import com.howei.pojo.Inform;

import java.util.List;
import java.util.Map;

public interface InformService {
    List<Map> getInformKPIList(Map map);

    List<Inform> getSelList(Map map);

    List<Inform> getCreatedList(Map map);
}
