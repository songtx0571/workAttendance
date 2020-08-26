package com.workattendance.service;

import com.workattendance.pojo.Menu;

import java.util.List;
import java.util.Map;

public interface MenuService {

    /**
     * 获取菜单
     * @param map
     * @return 当前级别的菜单
     */
    List<Menu> getMenuTree(Map map);
}
