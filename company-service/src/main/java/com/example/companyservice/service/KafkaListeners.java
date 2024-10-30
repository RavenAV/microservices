package com.example.companyservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaListeners {
    private final CompanyService companyService;

    public KafkaListeners(CompanyService companySevice) {
        this.companyService = companySevice;
    }

    // KafkaListeners — слушает сообщения о физическом удалении и удаляет компанию из базы данных.
    @KafkaListener(topics = "company-physical-deletion-topic", groupId = "company-service-group")
    public void handlePhysicalDeletion(String companyId) {
        Long id = Long.parseLong(companyId);
        companyService.physicalDeleteCompany(id);  // Физическое удаление компании
    }
}
