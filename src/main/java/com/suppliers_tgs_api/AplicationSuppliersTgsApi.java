package com.suppliers_tgs_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.suppliers_tgs_api.loader.EnvConfig;

@SpringBootApplication
public class AplicationSuppliersTgsApi {

	public static void main(String[] args) {
		EnvConfig.load();
		SpringApplication.run(AplicationSuppliersTgsApi.class, args);
		System.out.println("I am Alive!");
	}

}
