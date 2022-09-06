package com.esoft.hotelmanagementsystem;

import com.esoft.hotelmanagementsystem.repo.HotelTypeRepo;
import com.esoft.hotelmanagementsystem.service.UserService;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootApplication
@EnableWebSecurity
@EnableRabbit
public class HotelManagementSystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(HotelManagementSystemApplication.class, args);
		System.out.println("afsf");
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
