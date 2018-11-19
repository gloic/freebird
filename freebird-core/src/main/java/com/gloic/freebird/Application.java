package com.gloic.freebird;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Server's main class
 *
 * @author gloic
 */
@SpringBootApplication(scanBasePackages = {"com.gloic.freebird"})
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}