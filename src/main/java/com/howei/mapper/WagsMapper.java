package com.howei.mapper;

import com.howei.pojo.Tax;
import com.howei.pojo.Wages;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface WagsMapper {

    List<Wages> getWagsList(Map map);

    int copyToThisMonthWags(List<Wages> list);

    int updWags(Wages wages);

    List<Tax> getTaxList(Map map);

    Wages getWagsListById(@Param("id") String id);
}
