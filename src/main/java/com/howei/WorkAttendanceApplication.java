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
    //如果完成再研究一下，部门管理，创建中，1.添加公司的本部按钮是否还有用？2.添加部门中，部门代号时候还有用。3.创建完成后，提示失败，但是实际是成功的。3个小问题。

    public static void main(String[] args) {
        SpringApplication.run(WorkAttendanceApplication.class, args);
    }

}
