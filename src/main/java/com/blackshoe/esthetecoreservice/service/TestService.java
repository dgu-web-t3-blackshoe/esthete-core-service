package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.TestDto;
import com.blackshoe.esthetecoreservice.entity.ProfileImgUrl;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.repository.UserRepository;
import com.blackshoe.esthetecoreservice.vo.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class TestService {
    private final UserRepository userRepository;

    public TestDto.CreateTestUserResponse createTestUser(TestDto.CreateTestUserRequest request){

        ProfileImgUrl profileImgUrl = ProfileImgUrl.builder()
                .s3Url("")
                .cloudfrontUrl("")
                .build();

        User testUser = User.builder()
                .nickname(request.getNickname())
                .biography(request.getBiography())
                .build();

        testUser.setProfileImgUrl(profileImgUrl);

        userRepository.save(testUser);

        TestDto.CreateTestUserResponse response = TestDto.CreateTestUserResponse.builder()
                .userId(testUser.getUserId().toString())
                .profileImgUrl(testUser.getProfileImgUrl().getCloudfrontUrl())
                .createdAt(testUser.getCreatedAt().toString())
                .build();

        return response;
    }

    public TestDto.DeleteTestUserResponse deleteTestUser(UUID userId){

        User testUser = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Not Found User"));

        userRepository.delete(testUser);

        TestDto.DeleteTestUserResponse response = TestDto.DeleteTestUserResponse.builder()
                .userId(testUser.getUserId().toString())
                .deletedAt(String.valueOf(LocalDateTime.now()))
                .build();

        return response;
    }
}
