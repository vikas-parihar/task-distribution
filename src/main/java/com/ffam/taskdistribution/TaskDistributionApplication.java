package com.ffam.taskdistribution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ffam.taskdistribution"})
@EnableWebFlux
public class TaskDistributionApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(new Class[]{TaskDistributionApplication.class});
        app.run(args);
    }

}
