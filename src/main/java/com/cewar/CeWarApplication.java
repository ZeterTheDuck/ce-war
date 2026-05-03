package com.cewar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main application for website. Run this to start the entire webpage
 * 
 * Runs CeWarController.java to process web requests
 */
@SpringBootApplication
@ComponentScan(
    basePackages = {"com.cewar"}
    )
public class CeWarApplication {

	public static void main(String[] args) {
		SpringApplication.run(CeWarApplication.class, args);
	}

}
