package com.example.companyservice.service;

import com.example.companyservice.domain.Company;
import com.example.companyservice.model.CreateCompanyDto;
import com.example.companyservice.model.ViewCompanyDto;
import com.example.companyservice.repository.ICompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    private final ICompanyRepository companyRepository;

    public CompanyService(ICompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    // Получение списка всех компаний ( с ФИО директора)
    public List<ViewCompanyDto> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();

        return companies.stream().map(company -> {
            var companyDto = new ViewCompanyDto();
            companyDto.setId(company.getId());
            companyDto.setName(company.getName());
            companyDto.setOgrn(company.getOgrn());
            companyDto.setDescription(company.getDescription());
            companyDto.setDirectorId(company.getDirectorId());

            //companyDto.setDirectorName();
            return companyDto;
        }).collect(Collectors.toList());
    }

    // Проверка существования компании по ее идентификатору
    public boolean checkCompanyExisting(Long id) {
        return companyRepository.findById(id).isPresent();
    }

    // Создание компании
    public Long createCompany(CreateCompanyDto createCompanyDto) {
        // check existing director
        /*if (!companyService.companyExists(.getCompanyId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company does not exist");
        }*/

        var company = new Company();
        company.setName(createCompanyDto.getName());
        company.setOgrn(createCompanyDto.getOgrn());
        company.setDescription(createCompanyDto.getDescription());

        companyRepository.save(company);
        return company.getId();
    }
}
