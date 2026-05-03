package com.cewar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
// import org.springframework.context.annotation.FilterType;

/**
 * Main application for website. Run this to start the entire webpage
 * 
 * Runs CeWarController.java to process web requests
 */
@SpringBootApplication
@ComponentScan(
    basePackages = {"com.cewar"}
    // ,
    // excludeFilters= { @ComponentScan.Filter(
    //     type = FilterType.REGEX, 
    //     pattern = "com\\.cewar\\.example\\..*" // do not use files under com.cewar.example
    //     )}
    )
public class CeWarApplication {

	public static void main(String[] args) {
		SpringApplication.run(CeWarApplication.class, args);
	}

}
