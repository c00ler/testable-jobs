package com.github.avenderov.jobs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Profile("!test")
@Configuration
public class ExecutorConfig {

    @Bean
    public ScheduledExecutorService messagePrintingJobExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }
}
