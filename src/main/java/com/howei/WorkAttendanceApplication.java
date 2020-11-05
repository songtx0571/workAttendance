package com.howei;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = "com.howei")
@MapperScan("com.howei.mapper")
public class WorkAttendanceApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WorkAttendanceApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(WorkAttendanceApplication.class, args);
    }

}
