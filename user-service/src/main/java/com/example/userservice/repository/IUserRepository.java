package com.example.userservice.repository;

import com.example.userservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndEnabledTrue(Long id);
    String findNameById(Long Id);
    List<User> findByCompanyId(Long companyId);
}
