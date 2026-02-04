package com.coreline.financetracker.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.yourname.financetracker")
@EnableJpaRepositories(basePackages = "com.yourname.financetracker")
public class DatabaseConfig {
    // Relies on Spring Boot autoconfiguration
}
