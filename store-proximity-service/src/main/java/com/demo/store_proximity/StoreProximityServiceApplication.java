package com.demo.store_proximity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class StoreProximityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreProximityServiceApplication.class, args);
    }

}
