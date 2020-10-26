package com.howei.service.impl;

import com.howei.mapper.InformMapper;
import com.howei.pojo.Inform;
import com.howei.service.InformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InformServiceImpl implements InformService {

    @Autowired
    private InformMapper informMapper;

    @Override
    public List<Map> getInformKPIList(Map map) {
        return informMapper.getInformKPIList(map);
    }

    @Override
    public List<Inform> getSelList(Map map) {
        return informMapper.getSelList(map);
    }

    @Override
    public List<Inform> getCreatedList(Map map) {
        return informMapper.getCreatedList(map);
    }
}
