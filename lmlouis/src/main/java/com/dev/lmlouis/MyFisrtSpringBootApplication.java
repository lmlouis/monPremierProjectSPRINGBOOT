package com.dev.lmlouis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MyFisrtSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyFisrtSpringBootApplication.class, args);
	}

	@GetMapping("/hello")
	public String sayHello(@RequestParam(value = "myName", defaultValue = "Tout le Monde") String name) {
		return String.format("Salut %s !", name);
	}

}
