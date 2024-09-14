package com.tlab9.live.core;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // Custom health check logic
        boolean isHealthy = checkHealth();
        if (isHealthy) {
            return Health.up()
                .withDetail("customDetail", "Everything is OK!")
                .build();
        } else {
            return Health.down()
                .withDetail("customDetail", "Something is wrong!")
                .build();
        }
    }

    private boolean checkHealth() {
        // Implement your custom health check logic here
        return true; // Replace with actual health check logic
    }
}