package com.example.companyservice.repository;

import com.example.companyservice.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findById(Long id);
    Optional<Company> findByIdAndIsDeletedFalse(Long id);
    String findNameById(Long Id);
    List<Company> findByIsDeletedFalse();
}
