package com.business.money.controllers;

import com.business.money.DTOs.user.CreateUserDTO;
import com.business.money.DTOs.user.UserResponseDTO;
import com.business.money.entities.domain.ClanEntity;
import com.business.money.entities.domain.UserEntity;
import com.business.money.entities.security.AdminPermission;
import com.business.money.entities.security.UserPermission;
import com.business.money.exception.exceptions.NotFoundException;
import com.business.money.exception.exceptions.UserAlreadyExistsException;
import com.business.money.mappers.UserMapper;
import com.business.money.services.ClanService;
import com.business.money.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final ClanService clanService;

    @GetMapping
    @UserPermission
    public List<UserResponseDTO> getAllUsers(@RequestParam(required = false) String clan,
                                             @RequestParam(required = false) String email) throws NotFoundException {
        Set<UserEntity> res = new HashSet<>();

        if (clan != null) {
            ClanEntity clanEntity = clanService.findByName(clan);
            res = clanEntity.getMembers();
        }

        if (email != null) {
            Set<UserEntity> users = userService.findByEmailStartsWith(email);
            res.retainAll(users);
        }

        if (!res.isEmpty()) return res.stream().map(userMapper::toUserResponseDTO).toList();
        return userService.getAllUsers().stream().map(userMapper::toUserResponseDTO).toList();
    }

    @GetMapping("/{id}")
    @UserPermission
    public UserResponseDTO getUserById(@PathVariable Long id) throws NotFoundException {
        UserEntity foundUser = userService.findById(id);
        var response = userMapper.toUserResponseDTO(foundUser);
        response.setPlace(userService.getPlace(foundUser.getId()));
        return response;
    }

    @PostMapping
    @AdminPermission
    public UserResponseDTO saveNewUser(@RequestBody @Valid CreateUserDTO createUserDTO) throws NotFoundException, UserAlreadyExistsException {
        System.out.println("user creation");
        UserEntity user = userMapper.toEntity(createUserDTO);
        user = userService.save(user);
        return userMapper.toUserResponseDTO(user);
    }
}
