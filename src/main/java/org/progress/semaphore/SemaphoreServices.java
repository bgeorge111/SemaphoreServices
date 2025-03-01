package org.progress.semaphore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "org.progress.semaphore.*"})

@EnableAutoConfiguration
public class SemaphoreServices {
	public static void main(String[] args) {
		SpringApplication.run(SemaphoreServices.class, args);
	}
}