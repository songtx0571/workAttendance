package com.howei.controller;

import com.alibaba.fastjson.JSON;
import com.howei.pojo.Menu;
import com.howei.service.MenuService;
import com.howei.util.MenuTree;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/wa/menu")
@CrossOrigin(origins="http://192.168.1.27:8081",allowCredentials = "true")
public class MenuController {

    @Autowired
    MenuService menuService;

    @RequestMapping("/getMenuTree")
    @ResponseBody
    public String getMenuTree(HttpSession session, HttpServletRequest request){
        String id= request.getParameter("id");
        Subject subject=SecurityUtils.getSubject();
        Map map=new HashMap();
        map.put("parentId",id);
        map.put("template","2");
        List<MenuTree> resultList=new ArrayList<>();
        List<Menu> menuList=menuService.getMenuTree(map);
        Iterator<Menu> iterator = menuList.iterator();
        while(iterator.hasNext()){
            Menu menu = iterator.next();
            if(!subject.isPermitted(menu.getName())){
                iterator.remove();
            }
        }
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
