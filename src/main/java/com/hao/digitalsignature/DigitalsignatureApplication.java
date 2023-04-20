package com.hao.digitalsignature;

import org.mapstruct.MapperConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hao.digitalsignature.mapper")
public class DigitalsignatureApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalsignatureApplication.class, args);
    }

}
