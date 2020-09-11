package com.workattendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wa/employee")
//@RequestMapping("/employee")
public class EmployeeController {
    @RequestMapping("/toEmployee")
    public String toEmployee(){
        return "employee";
    }

}
