package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.dto.TestDto;
import com.blackshoe.esthetecoreservice.service.TestService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/core") @RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @PostMapping("/test-user")
    public ResponseEntity<TestDto.CreateTestUserResponse> testUser(@RequestBody TestDto.CreateTestUserRequest request) {
        TestDto.CreateTestUserResponse response = testService.createTestUser(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @DeleteMapping("/test-user/{userId}")
    public ResponseEntity<TestDto.DeleteTestUserResponse> deleteTestUser(@PathVariable UUID userId) {
        TestDto.DeleteTestUserResponse response = testService.deleteTestUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
