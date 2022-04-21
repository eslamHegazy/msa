package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
//		Class<?>[] runner = new Class<?>[]{CrudRunner.class};
//		System.exit(SpringApplication.exit(SpringApplication.run(runner, args)));
		SpringApplication.run(DemoApplication.class, args);
	}

}
