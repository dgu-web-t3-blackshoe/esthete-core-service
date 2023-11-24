package com.blackshoe.esthetecoreservice.service;

import org.springframework.kafka.support.Acknowledgment;

public interface KafkaUserInfoConsumerService{
    void createUser(String payload, Acknowledgment acknowledgment);
}
