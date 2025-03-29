package com.ant.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class UserServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApp.class, args);
	}
}
