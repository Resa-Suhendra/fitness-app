package com.resa.fitness;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * Created by Resa S.
 * Date: 22-10-2023
 * Created in IntelliJ IDEA.
 */
@Configuration
public class CustomFlywayConfiguration extends FlywayAutoConfiguration.FlywayConfiguration {
    @DependsOn("fitnessApp")
    @Bean("flywayInitializer")
    public FlywayMigrationInitializer flywayInitializer(Flyway flyway) {
        return super.flywayInitializer(flyway, null);
    }
}