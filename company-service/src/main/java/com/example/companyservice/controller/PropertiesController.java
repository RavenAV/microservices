package com.example.companyservice.controller;

import com.example.companyservice.config.ServiceDescriptionConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class PropertiesController {
    private final ServiceDescriptionConfig descriptionConfig;

    @GetMapping("/description")
    public String getServiceDescription() {
        return descriptionConfig.getDescription();
    }
}
