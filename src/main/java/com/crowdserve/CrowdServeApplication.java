package com.crowdserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the CrowdServe application.
 * A localized, real-time crowdsourcing platform.
 */
@SpringBootApplication
public class CrowdServeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrowdServeApplication.class, args);
    }
}
