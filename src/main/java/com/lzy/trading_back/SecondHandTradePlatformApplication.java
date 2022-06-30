package com.lzy.trading_back;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lzy.trading_back.mapper")
public class SecondHandTradePlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondHandTradePlatformApplication.class, args);
    }

}
