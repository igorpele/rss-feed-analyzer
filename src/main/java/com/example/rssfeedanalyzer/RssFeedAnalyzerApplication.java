package com.example.rssfeedanalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RssFeedAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RssFeedAnalyzerApplication.class, args);
	}

}
