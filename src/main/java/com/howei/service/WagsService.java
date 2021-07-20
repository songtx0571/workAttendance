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

    Wages getWagesByMap(Map map);

    int addThisMonthWags(Wages wages);

    List<Wages> getWagesToTax(Map map);
}
