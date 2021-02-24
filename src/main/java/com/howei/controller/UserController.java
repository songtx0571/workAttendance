package com.howei.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.howei.pojo.Menu;
import com.howei.pojo.Users;
import com.howei.service.EmployeeService;
import com.howei.service.MenuService;
import com.howei.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
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
//@CrossOrigin(origins="http://test.hopeop.com",allowCredentials = "true")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    MenuService menuService;

    @Autowired
    EmployeeService employeeService;

    /*存入session里的用户名称*/
    public static final String SESSION_USER = "sessionUser";

    @RequestMapping("/")
    @CrossOrigin
    public String login(HttpSession session){
        return "login";
    }

    @RequestMapping("/home")
    @CrossOrigin
    public String home(HttpSession session){
        return "home";
    }

    @RequestMapping("/log")
    @ResponseBody
    public String log(){
        return "您没有权限访问";
    }

    public Users getPrincipal(){
        Subject subject=SecurityUtils.getSubject();
        Users users=(Users)subject.getPrincipal();
        return users;
    }

    @RequestMapping("/loginPage")
    public String loginadmin(HttpServletRequest request) {
        String username = request.getParameter("userNumber").toUpperCase();
        String password = request.getParameter("password");

        UsernamePasswordToken upt = new UsernamePasswordToken(username,password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(upt);
        } catch (Exception e) {
            e.printStackTrace();
            if("no_user".equals(e.getMessage())){
                return JSON.toJSONString("用户不存在");
            }else if("no_permission".equals(e.getMessage())){
                return JSON.toJSONString("用户无权限访问");
            }else if("no_status".equals(e.getMessage())){
                return JSON.toJSONString("用户已停用");
            }
            return JSON.toJSONString("密码错误");
        }
        Session session = subject.getSession();
        Users user = userService.loginUserNumber(username);
        user.setPassword("");
        session.setAttribute(SESSION_USER, user);
        return "home";
    }

    @RequestMapping("/getMenu")
    @ResponseBody
    @CrossOrigin
    public String getMenuTree(HttpServletRequest request){
        String parentId=request.getParameter("parent");
        Map map=new HashMap();
        map.put("parentId",parentId);
        map.put("template","2");
        Subject subject=SecurityUtils.getSubject();
        List<Menu> result=menuService.getMenuTree(map);
        Iterator<Menu> iterator = result.iterator();
        while(iterator.hasNext()){
            Menu menu = iterator.next();
            if(!subject.isPermitted(menu.getName())){
                iterator.remove();
            }
        }
        return JSON.toJSONString(result);
    }

    //获取员工所属项目部信息
    @ResponseBody
    @RequestMapping("/getNameByProjectId")
    public List<Map<String,Object>> getNameByProjectId(HttpServletRequest request) {
        String projectId=request.getParameter("projectId");//部门
        List<Map<String,Object>> list=new ArrayList<>();
        List<Users> userList=userService.getNameByProjectId(Integer.parseInt(projectId));
        if(userList!=null){
            for(int i=0;i<userList.size();i++){
                Users users=userList.get(i);
                Map<String,Object> map=new HashMap<>();
                map.put("id",users.getUserNumber());
                map.put("text",users.getUserName());
                list.add(map);
            }
        }
        return list;
    }

}
