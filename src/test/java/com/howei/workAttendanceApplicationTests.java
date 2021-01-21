package com.howei;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class workAttendanceApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("1");
    }

    @Test
    void bigDecimal() {
        BigDecimal bigDecimal = new BigDecimal("");
    }

}
