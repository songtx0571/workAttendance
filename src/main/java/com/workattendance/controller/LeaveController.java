package com.workattendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/leave")
//@RequestMapping("/leave")
public class LeaveController {
    @RequestMapping("/toLeave")
    public String toLeave(){
        return "leave";
    }
    @RequestMapping("/toLeaveConfigure")
    public String toLeaveConfigure(){
        return "leaveConfigure";
    }
}
