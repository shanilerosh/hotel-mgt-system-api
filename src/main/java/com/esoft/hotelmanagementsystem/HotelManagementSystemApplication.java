package com.esoft.hotelmanagementsystem;

import com.esoft.hotelmanagementsystem.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@EnableWebSecurity
public class HotelManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelManagementSystemApplication.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner run(UserService userService) {
		return args -> {
//		userService.saveRole(new Role(null, "ROLE_USER"));
//
//		userService.saveUser(new UserMst(null, "Shanil", "shanil", "shanil",new ArrayList<>()));
//
//		userService.addRoleToUser("shanil","ROLE_USER");
		};
	}

}
