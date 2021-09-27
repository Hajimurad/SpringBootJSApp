package com.springboot_rest_js;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpringBootRestJsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootRestJsApplication.class, args);
    }
}
