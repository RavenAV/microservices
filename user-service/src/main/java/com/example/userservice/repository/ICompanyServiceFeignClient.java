package com.example.userservice.repository;

import com.example.userservice.feignClientModel.CompanyShortInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
   name = "company-service",
   path = "/companies",
   url = "http://localhost:8089"
)
public interface ICompanyServiceFeignClient {
    @GetMapping("/exists/{id}")
    Boolean companyExists(@PathVariable("id") Long id);

    @GetMapping("/name/{id}")
    String getCompanyName(@PathVariable("id") Long id);

    @GetMapping("/getAllCompaniesShortInfo")
    List<CompanyShortInfoDto> getAllCompaniesShortInfo();
}
