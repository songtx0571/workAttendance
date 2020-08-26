package com.workattendance.controller;

import com.alibaba.fastjson.JSON;
import com.workattendance.pojo.Menu;
import com.workattendance.service.MenuService;
import com.workattendance.util.MenuTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wa/menu")
public class MenuController {

    @Autowired
    MenuService menuService;

    @RequestMapping("/getMenuTree")
    @ResponseBody
    public String getMenuTree(HttpSession session, HttpServletRequest request){
        Integer userId=(Integer)session.getAttribute("userId");
        String id= request.getParameter("id");
        Map map=new HashMap();
        map.put("parentId",id);
        map.put("template","2");
        List<MenuTree> resultList=new ArrayList<>();
        List<Menu> menuList=menuService.getMenuTree(map);
        for(Menu menu:menuList){
            MenuTree menuTree=new MenuTree();
            menuTree.setId(String.valueOf(menu.getId()));
            menuTree.setText(menu.getName());
            menuTree.setState("close");
            menuTree.setpId(String.valueOf(menu.getParent()));
            menuTree.setIconCls("icon-bullet-blue");
            menuTree.setUrl(menu.getUrl());
            resultList.add(menuTree);
        }
        String json = JSON.toJSONString(resultList);
        return json;
    }
}
