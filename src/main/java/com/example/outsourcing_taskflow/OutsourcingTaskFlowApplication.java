package com.example.outsourcing_taskflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OutsourcingTaskFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(OutsourcingTaskFlowApplication.class, args);
    }

}
