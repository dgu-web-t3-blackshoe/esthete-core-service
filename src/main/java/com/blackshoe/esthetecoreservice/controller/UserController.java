package com.blackshoe.esthetecoreservice.controller;


import com.blackshoe.esthetecoreservice.dto.UserDto;
import com.blackshoe.esthetecoreservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/core/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{user_id}/equipments")
    ResponseEntity<UserDto.ReadEquipmentsResponse> getEquipments(@PathVariable(name = "user_id") UUID userId) {
        //getEquipmentsForUser
        UserDto.ReadEquipmentsResponse response = userService.getEquipmentsForUser(userId);

        return ResponseEntity.ok(response);
    }
}
