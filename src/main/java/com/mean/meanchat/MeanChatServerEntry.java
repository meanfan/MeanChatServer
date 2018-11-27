package com.mean.meanchat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@MapperScan("com.mean.meanchat.mapper")
public class MeanChatServerEntry {

    @RequestMapping("/test")
    String index(){
        return "Hello";
    }

    public static void main(String[] args) {
        SpringApplication.run(MeanChatServerEntry.class, args);
    }
}
