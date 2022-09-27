package com.profilebaba.googledata;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@Slf4j
public class GoogleDataApplication {


	public static void main(String[] args) {
		SpringApplication.run(GoogleDataApplication.class, args);
	}
}
