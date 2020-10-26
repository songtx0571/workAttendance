package com.howei.service;

import com.howei.pojo.Menu;

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
