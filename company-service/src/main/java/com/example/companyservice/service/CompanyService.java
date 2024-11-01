package com.example.companyservice.service;

import com.example.companyservice.domain.Company;
import com.example.companyservice.feignClientModel.CompanyShortInfoDto;
import com.example.companyservice.feignClientModel.UserShortInfoDto;
import com.example.companyservice.kafka.KafkaProducerService;
import com.example.companyservice.model.CreateCompanyDto;
import com.example.companyservice.model.ViewCompanyDto;
import com.example.companyservice.repository.ICompanyRepository;
import com.example.companyservice.repository.IUserServiceFeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    private final ICompanyRepository companyRepository;
    private final IUserServiceFeignClient userServiceFeignClient;
    private final KafkaProducerService kafkaProducerService;

    public CompanyService(ICompanyRepository companyRepository, IUserServiceFeignClient userServiceFeignClient, KafkaProducerService kafkaProducerService) {
        this.companyRepository = companyRepository;
        this.userServiceFeignClient = userServiceFeignClient;
        this.kafkaProducerService = kafkaProducerService;
    }

    // Получение списка всех компаний (с ФИО директора)
    public List<ViewCompanyDto> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        List<UserShortInfoDto> users = userServiceFeignClient.getAllUsersShortInfo();

        return companies.stream().filter(company -> !company.getIsDeleted()).map(company -> {
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
        if (createCompanyDto.getDirectorId() != null) {
            Boolean isExistCompany = userServiceFeignClient.userExists(createCompanyDto.getDirectorId());
            if (!isExistCompany) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Director does not exist");
            }
        }

        var company = new Company();
        company.setName(createCompanyDto.getName());
        company.setOgrn(createCompanyDto.getOgrn());
        company.setDescription(createCompanyDto.getDescription());
        company.setDirectorId(createCompanyDto.getDirectorId());

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

    // создаём эндпоинт для мягкого удаления компании и отправляем сообщение в Kafka
    public boolean softDeleteCompany(Long id) {
        Optional<Company> companyOpt = companyRepository.findById(id);
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();
            company.setIsDeleted(true);  // Помечаем компанию как удаленную
            companyRepository.saveAndFlush(company);  // Сохраняем изменение в базе данных
            kafkaProducerService.send("delete-company", id.toString(), id.toString());
            return true;
        }
        return false;
    }
}
