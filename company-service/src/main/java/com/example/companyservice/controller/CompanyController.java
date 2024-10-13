package com.example.companyservice.controller;

import com.example.companyservice.feignClientModel.CompanyShortInfoDto;
import com.example.companyservice.model.CreateCompanyDto;
import com.example.companyservice.model.ViewCompanyDto;
import com.example.companyservice.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/getAllCompanies")
    public List<ViewCompanyDto> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @PostMapping("/createCompany")
    public ResponseEntity<Long> createCompany(@RequestBody CreateCompanyDto companyDto) {
        var companyId = companyService.createCompany(companyDto);
        return ResponseEntity.ok(companyId);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> companyExists(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.checkCompanyExisting(id));
    }

    @GetMapping("/name/{id}")
    public ResponseEntity<String> getCompanyName(@PathVariable Long id) {
        var name = companyService.getCompanyName(id);
        return ResponseEntity.ok(name);
    }

    @GetMapping("/getAllCompaniesShortInfo")
    public List<CompanyShortInfoDto> getAllCompaniesShortInfo() {
        return companyService.getAllCompaniesShortInfo();
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
    }
}
