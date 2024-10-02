package com.example.companyservice.service;

import com.example.companyservice.domain.Company;
import com.example.companyservice.feignClientModel.CompanyShortInfoDto;
import com.example.companyservice.feignClientModel.UserShortInfoDto;
import com.example.companyservice.model.CreateCompanyDto;
import com.example.companyservice.model.ViewCompanyDto;
import com.example.companyservice.repository.ICompanyRepository;
import com.example.companyservice.repository.IUserServiceFeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    private final ICompanyRepository companyRepository;
    private final IUserServiceFeignClient userServiceFeignClient;

    public CompanyService(ICompanyRepository companyRepository, IUserServiceFeignClient userServiceFeignClient) {
        this.companyRepository = companyRepository;
        this.userServiceFeignClient = userServiceFeignClient;
    }

    // Получение списка всех компаний ( с ФИО директора)
    public List<ViewCompanyDto> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        List<UserShortInfoDto> users = userServiceFeignClient.getAllUsersShortInfo();

        return companies.stream().map(company -> {
            var companyDto = new ViewCompanyDto();
            companyDto.setId(company.getId());
            companyDto.setName(company.getName());
            companyDto.setOgrn(company.getOgrn());
            companyDto.setDescription(company.getDescription());
            companyDto.setDirectorId(company.getDirectorId());

            String directorName = users.stream()
                    .filter(u -> u.getId().equals(company.getDirectorId()))
                    .findFirst()
                    .map(u -> u.getName())
                    .orElse("");
            companyDto.setDirectorName(directorName);

            return companyDto;
        }).collect(Collectors.toList());
    }

    // Проверка существования компании по ее идентификатору
    public boolean checkCompanyExisting(Long id) {
        return companyRepository.findById(id).isPresent();
    }

    // Создание компании
    public Long createCompany(CreateCompanyDto createCompanyDto) {
        Boolean isExistCompany = userServiceFeignClient.userExists(createCompanyDto.getDirectorId());
        if (!isExistCompany) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Director does not exist");
        }

        var company = new Company();
        company.setName(createCompanyDto.getName());
        company.setOgrn(createCompanyDto.getOgrn());
        company.setDescription(createCompanyDto.getDescription());

        companyRepository.save(company);
        return company.getId();
    }

    public String getCompanyName(Long id) {
        return companyRepository.findNameById(id);
    }

    public List<CompanyShortInfoDto> getAllCompaniesShortInfo() {
        List<Company> companies = companyRepository.findAll();

        return companies.stream().map(user -> {
            var companyDto = new CompanyShortInfoDto();
            companyDto.setId(user.getId());
            companyDto.setName(user.getName());

            return companyDto;
        }).collect(Collectors.toList());
    }
}
