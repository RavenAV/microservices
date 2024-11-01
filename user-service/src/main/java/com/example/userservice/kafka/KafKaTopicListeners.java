package com.example.userservice.kafka;

import com.example.userservice.domain.User;
import com.example.userservice.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafKaTopicListeners {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    KafkaProducerService kafkaProducerService;

    /* При получении сообщения в юзер сервисе, ищутся все пользователи, работающие в данной компании,
     * и поле company_id для них сбрасывается в null.
     * После этого отправляется сообщение в company service, по которому компания физически удалятся из БД.
     */
    @KafkaListener(topics = "delete-company", groupId = "group2")
    public void consume(Long companyId) {
        System.out.println("user-service получил: " + companyId);

        List<User> users = userRepository.findAll();

        for (var user : users) {
            if (user.getCompanyId() == companyId) {
                user.setCompanyId(null);
            }
        }

        userRepository.saveAllAndFlush(users);

        kafkaProducerService.send("company-deleted", companyId.toString(), companyId.toString());
    }
}
