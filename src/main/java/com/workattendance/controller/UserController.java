package com.workattendance.controller;

import com.alibaba.fastjson.JSONObject;
import com.workattendance.pojo.Menu;
import com.workattendance.pojo.Users;
import com.workattendance.service.MenuService;
import com.workattendance.service.UserService;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    MenuService menuService;

    @RequestMapping("/")
    public String login(HttpSession session, HttpServletRequest request){
        if(session.getAttribute("userId")!=null){
            return "home";
        }
        return "redirect:/login";
    }

    @RequestMapping("/login")
    public String index(HttpSession session, HttpServletRequest request){
        if(session.getAttribute("userId")!=null){
            return "home";
        }
        return "login";
    }

    @RequestMapping(value = "/loginPage", method = {RequestMethod.POST, RequestMethod.GET})
    public String loginadmin(HttpServletRequest request, HttpSession session) {
        String UserName = request.getParameter("UserName");
        String Password = request.getParameter("Password");
        Users users = userService.findUser(UserName, Password);
        if (users!=null) {
            session.setAttribute("userId", users.getId());//编号
            session.setAttribute("roleId", users.getRoleId());//角色id
            session.setAttribute("projectId", users.getProjectId());//项目部id
            session.setAttribute("userName",users.getName());//当前用户名
            return "home";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping("/getMenu")
    @ResponseBody
    public List<Menu> getMenuTree(HttpSession session, HttpServletRequest request){
        String parentId=request.getParameter("parent");
        Map map=new HashMap();
        map.put("parentId",parentId);
        map.put("template","2");
        List<Menu> result=menuService.getMenuTree(map);
        return result;
    }

    // 获取员工所属项目部信息
    @ResponseBody
    @RequestMapping(value = "/getProjectIdS", produces = "text/json;charset=UTF-8")
    public String getProjectIdS(HttpServletRequest request) {
        String userName=request.getParameter("userName");
        String pageStr=request.getParameter("page");
        Integer page=Integer.parseInt(pageStr);
        String limit=request.getParameter("rows");
        Integer rows=Integer.parseInt(limit);
        List<Users> users = null;
        users = userService.getProjectIdS(userName);
        Integer size = users.size();
        if (page == null && limit == null) {

        } else {
            List<Users> users2 = new ArrayList<>();
            for (int i = (page - 1) * rows; i < page * (rows); i++) {
                if (i < users.size()) {
                    users2.add(users.get(i));
                }
            }
            users = users2;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", size);
        map.put("data", users);
        return JSONObject.toJSONString(map);
    }

    //获取员工所属项目部信息
    @ResponseBody
    @RequestMapping("/getNameByProjectId")
    public List<Map<String,Object>> getNameByProjectId(HttpServletRequest request) {
        String projectId=request.getParameter("projectId");
        List<Map<String,Object>> list=new ArrayList<>();
        List<Users> userList=userService.getNameByProjectId(Integer.parseInt(projectId));
        if(userList!=null){
            for(int i=0;i<userList.size();i++){
                Users users=userList.get(i);
                Map<String,Object> map=new HashMap<>();
                map.put("id",users.getUserName());
                map.put("text",users.getName());
                list.add(map);
            }
        }
        return list;
    }

}
