package com.suppliers_tgs_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class AplicationSuppliersTgsApi {

public static void main(String[] args) {
    try {
        System.out.println("ENV loaded");
        
        SpringApplication.run(AplicationSuppliersTgsApi.class, args);

        System.out.println("I am Alive!");
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}