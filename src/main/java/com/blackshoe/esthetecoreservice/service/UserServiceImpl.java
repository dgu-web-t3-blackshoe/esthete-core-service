package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.UserDto;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.exception.UserErrorResult;
import com.blackshoe.esthetecoreservice.exception.UserException;
import com.blackshoe.esthetecoreservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto.GetBasicInfoResponse getBasicInfo(UUID userId) {

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        UserDto.GetBasicInfoResponse userGetBasicInfoResponse = UserDto.GetBasicInfoResponse.builder()
                .userId(user.getUserId().toString())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImgUrl().getCloudfrontUrl())
                .build();

        return userGetBasicInfoResponse;
    }
}
