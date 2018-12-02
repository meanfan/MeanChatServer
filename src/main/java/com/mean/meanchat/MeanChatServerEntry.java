package com.mean.meanchat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@MapperScan("com.mean.meanchat.mapper")
public class MeanChatServerEntry {

    public static void main(String[] args) {
        SpringApplication.run(MeanChatServerEntry.class, args);
    }
}
