package com.example.companyservice.controller;

import com.example.companyservice.model.CreateCompanyDto;
import com.example.companyservice.model.ViewCompanyDto;
import com.example.companyservice.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {
    private CompanyService companyService;

    @GetMapping
    public List<ViewCompanyDto> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @PostMapping
    public ResponseEntity<Long> createCompany(@RequestBody CreateCompanyDto companyDto) {
        var companyId = companyService.createCompany(companyDto);
        return ResponseEntity.ok(companyId);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Void> companyExists(@PathVariable Long id) {
        if (!companyService.checkCompanyExisting(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
        }
        return ResponseEntity.ok().build();
    }
}
