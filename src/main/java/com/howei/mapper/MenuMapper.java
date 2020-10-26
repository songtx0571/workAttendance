package com.howei.mapper;

import com.howei.pojo.Menu;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface MenuMapper {

    List<Menu> getMenuTree(Map map);


}
