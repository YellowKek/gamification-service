package com.business.money.util;

import com.business.money.DTOs.user.UserLoginDTO;
import com.business.money.DTOs.user.UserLoginResponseDTO;
import com.business.money.entities.domain.UserEntity;
import com.business.money.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AuthUtil {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserLoginResponseDTO responseAuthUser(UserEntity user, UserLoginDTO userDto) throws BadCredentialsException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword())
        );
        String token = jwtUtil.createToken(user);
        UserEntity foundUser = userService.findByEmail(userDto.getEmail());

        return new UserLoginResponseDTO(foundUser.getId(), userDto.getEmail(), token);
    }
}
