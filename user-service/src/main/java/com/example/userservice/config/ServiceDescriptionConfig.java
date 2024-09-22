package com.example.userservice.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class ServiceDescriptionConfig {
    @Value("${custom.property.description}")
    private String description;
}
