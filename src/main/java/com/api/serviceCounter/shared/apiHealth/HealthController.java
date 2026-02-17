package com.api.serviceCounter.shared.apiHealth;

import org.springframework.boot.health.actuate.endpoint.HealthDescriptor;
import org.springframework.boot.health.actuate.endpoint.HealthEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    private final HealthEndpoint healthEndpoint;

    public HealthController(HealthEndpoint healthEndpoint){
        this.healthEndpoint = healthEndpoint;
    }

    @GetMapping("/health")
    public Map<String, Object> health(){
        HealthDescriptor health = healthEndpoint.health();
        return Map.of("status", health.getStatus().getCode());
    }

}
