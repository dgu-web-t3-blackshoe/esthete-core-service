package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.UserDto;

public interface KafkaUserInfoProducerService {

    void deleteUser(UserDto.UserInfoDto userInfoDto);

}
