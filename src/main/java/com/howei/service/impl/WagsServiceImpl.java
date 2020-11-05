package com.howei.service.impl;

import com.howei.mapper.WagsMapper;
import com.howei.pojo.Tax;
import com.howei.pojo.Wages;
import com.howei.service.WagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WagsServiceImpl implements WagsService {

    @Autowired
    private WagsMapper wagsMapper;

    @Override
    public List<Wages> getWagsList(Map map) {
        return wagsMapper.getWagsList(map);
    }

    @Override
    public int copyToThisMonthWags(List<Wages> list) {
        return wagsMapper.copyToThisMonthWags(list);
    }

    @Override
    public int updWags(Wages wages) {
        return wagsMapper.updWags(wages);
    }

    @Override
    public List<Tax> getTaxList(Map map) {
        return wagsMapper.getTaxList(map);
    }

    @Override
    public Wages getWagsListById(String id) {
        return wagsMapper.getWagsListById(id);
    }
}
