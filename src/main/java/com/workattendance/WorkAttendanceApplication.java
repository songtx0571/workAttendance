package com.workattendance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.workattendance")
@MapperScan("com.workattendance.mapper")
public class WorkAttendanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkAttendanceApplication.class, args);
    }

}
