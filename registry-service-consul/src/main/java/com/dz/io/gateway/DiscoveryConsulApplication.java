package com.dz.io.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DiscoveryConsulApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryConsulApplication.class, args);
	}

}
