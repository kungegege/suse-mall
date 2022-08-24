package com.frame;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.frame.business.mapper")
public class CompanyFrameApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompanyFrameApplication.class, args);
    }
}
