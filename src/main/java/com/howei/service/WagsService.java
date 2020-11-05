package com.howei.service;

import com.howei.pojo.Tax;
import com.howei.pojo.Wages;

import java.util.List;
import java.util.Map;

public interface WagsService {

    List<Wages> getWagsList(Map map);

    int copyToThisMonthWags(List<Wages> list);

    int updWags(Wages wages);

    List<Tax> getTaxList(Map map);

    Wages getWagsListById(String id);
}
