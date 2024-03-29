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
    public Wages getWagesByMap(Map map) {
        return wagsMapper.getWagsByMap(map);
    }

    @Override
    public int addThisMonthWags(Wages wages) {
        return wagsMapper.addThisMonthWags(wages);
    }

    @Override
    public List<Wages> getWagesToTax(Map map) {
        return wagsMapper.getWagesToTax(map);
    }

    @Override
    public int deleteByDate(String date) {
        return wagsMapper.deleteByDate(date);
    }

    @Override
    public List<Wages> getSimpleWagsListByMap(Map map) {
        return wagsMapper.getSimpleWagsListByMap(map);
    }
}
