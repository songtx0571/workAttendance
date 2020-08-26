package com.workattendance.mapper;

import com.workattendance.pojo.Menu;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface MenuMapper {

    List<Menu> getMenuTree(Map map);


}
