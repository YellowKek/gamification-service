package com.business.money.controllers;

import com.business.money.DTOs.user.UserLoginDTO;
import com.business.money.DTOs.user.UserLoginResponseDTO;
import com.business.money.services.UserService;
import com.business.money.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final AuthUtil authUtil;

    @PostMapping("/login")
    public UserLoginResponseDTO login(@RequestBody UserLoginDTO loginUserDto) {
        System.out.println(loginUserDto);
        var user = userService.findByEmail(loginUserDto.getEmail());
        return authUtil.responseAuthUser(user, loginUserDto);
    }
}
