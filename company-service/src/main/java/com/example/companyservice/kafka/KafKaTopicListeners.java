package com.example.companyservice.kafka;

import com.example.companyservice.repository.ICompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafKaTopicListeners {

    @Autowired
    ICompanyRepository companyRepository;

    /* После этого отправляется сообщение в company service, по которому компания физически удалятся из БД.
     */
    @KafkaListener(topics = "company-deleted", groupId = "group1")
    public void consume(Long companyId) {
        System.out.println("company-service получил: " + companyId);

        companyRepository.deleteById(companyId);
    }
}
