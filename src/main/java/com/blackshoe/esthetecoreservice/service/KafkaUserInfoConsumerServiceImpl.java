package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.UserDto;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.repository.UserRepository;
import com.blackshoe.esthetecoreservice.vo.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j @RequiredArgsConstructor @Service
public class KafkaUserInfoConsumerServiceImpl implements KafkaUserInfoConsumerService{

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    @Override
    @KafkaListener(topics = "user-create")
    @Transactional
    public void createUser(String payload, Acknowledgment acknowledgment) {
        log.info("received payload = '{}'", payload);

        UserDto.UserInfoDto userInfoDto;

        try {
            userInfoDto = objectMapper.readValue(payload, UserDto.UserInfoDto.class);
        } catch (Exception e) {
            log.error("Error converting to user info dto", e);
            return;
        }

        UUID userId = userInfoDto.getUserId();
        Role role = Role.valueOf(userInfoDto.getRole());

        User user = User.builder()
                .userId(userId)
                .email(userInfoDto.getEmail())
                .role(role)
                .build();

        try {
            userRepository.save(user);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Error saving user nickname", e);
            return;
        }
        acknowledgment.acknowledge();
    }

}
