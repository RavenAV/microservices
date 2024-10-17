package com.example.companyservice.repository;

import com.example.companyservice.feignClientModel.UserShortInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
   name = "user-service",
   path = "/users",
   url = "http://localhost:8082"
)
public interface IUserServiceFeignClient {
    @GetMapping("/exists/{id}")
    Boolean userExists(@PathVariable("id") Long id);

    @GetMapping("/name/{id}")
    String getUserName(@PathVariable("id") Long id);

    @GetMapping("/getAllUsersShortInfo")
    List<UserShortInfoDto> getAllUsersShortInfo();
}
