package com.suppliers_tgs_api.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsyncConfig {

    @Bean
    public ExecutorService providerExecutor() {
        return Executors.newFixedThreadPool(10);
    }
}