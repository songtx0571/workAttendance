package com.workattendance.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/wa/achievements")
public class AchievementsController {
    @RequestMapping("/toAchievements")
    public String toAchievements(){
        return "achievements";
    }
}