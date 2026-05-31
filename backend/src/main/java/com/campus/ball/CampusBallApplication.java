package com.campus.ball;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CampusBallApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CampusBallApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(CampusBallApplication.class, args);
        System.out.println("校园约球平台后端服务启动成功！");
        System.out.println("接口文档地址：http://localhost:8080/api/swagger-ui.html");
    }
}
