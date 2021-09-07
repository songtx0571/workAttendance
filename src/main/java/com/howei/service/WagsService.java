package com.howei.service;

import com.howei.pojo.Tax;
import com.howei.pojo.Wages;

import java.util.List;
import java.util.Map;

public interface WagsService {
    /**
     * 查询工资表,多表列连接
     *
     * @param map
     * @return
     */
    List<Wages> getWagsList(Map map);

    int copyToThisMonthWags(List<Wages> list);

    int updWags(Wages wages);

    List<Tax> getTaxList(Map map);

    Wages getWagesByMap(Map map);

    int addThisMonthWags(Wages wages);

    List<Wages> getWagesToTax(Map map);

    int deleteByDate(String date);

    /**
     * 查询工资表
     *
     * @param map
     * @return
     */
    List<Wages> getSimpleWagsListByMap(Map map);
}
