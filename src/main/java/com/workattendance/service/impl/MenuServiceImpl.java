package com.workattendance.service.impl;

import com.workattendance.mapper.MenuMapper;
import com.workattendance.pojo.Menu;
import com.workattendance.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuMapper menuMapper;

    @Override
    public List<Menu> getMenuTree(Map map) {
        return menuMapper.getMenuTree(map);
    }
}
