package com.profilebaba.googledata;

import com.profilebaba.googledata.service.impl.GoogleScheduledService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@Slf4j
public class GoogleDataApplication {

	@Autowired
	private GoogleScheduledService googleScheduledService;

	public static void main(String[] args) {
		SpringApplication.run(GoogleDataApplication.class, args);
	}

	@Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}")
	public void scheduleSearchService() {
		log.info("scheduleSearchService started");
		googleScheduledService.fetchAndStoreGoogleSearchData();
		log.info("scheduleSearchService finished");
	}

}
