package com.example.userservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaListeners {
    private final UserService userService;

    public KafkaListeners(UserService userService) {
        this.userService = userService;
    }

    // Обрабатываем удаление, сбрасывая company_id у пользователей, и отправляем сообщение обратно в Kafka для физического удаления компании
    @KafkaListener(topics = "company-deletion-topic", groupId = "user-service-group")
    public void handleCompanyDeletion(String companyId) {
        Long id = Long.parseLong(companyId);
        userService.resetCompanyForUsers(id);
    }
}
