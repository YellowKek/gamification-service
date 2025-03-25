package com.business.money.services;

import com.business.money.entities.domain.ClanEntity;
import com.business.money.entities.domain.RoleEntity;
import com.business.money.entities.domain.UserEntity;
import com.business.money.entities.security.Roles;
import com.business.money.exception.exceptions.NotFoundException;
import com.business.money.exception.exceptions.UserAlreadyExistsException;
import com.business.money.repos.UserRepo;
import com.business.money.util.PasswordGenerator;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final ClanService clanService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;

    public List<UserEntity> getAllUsers() {
        return userRepo.findAll();
    }

    @Transactional
    public UserEntity findByEmail(String email) throws UsernameNotFoundException {
        Optional<UserEntity> foundUser = userRepo.findByEmail(email);
        if (foundUser.isEmpty()) throw new UsernameNotFoundException("Пользователя с такой почтой не сущетвует");
        return foundUser.get();
    }

    public UserEntity findById(Long id) throws NotFoundException {
        return userRepo.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public Set<UserEntity> findByEmailStartsWith(String email) {
        return userRepo.findAllByEmailStartsWith(email);
    }

    @Transactional
    public UserEntity save(UserEntity user) throws UserAlreadyExistsException, NotFoundException {
        if (userRepo.findByEmail(user.getEmail()).isPresent())
            throw new UserAlreadyExistsException("Пользователь с такой почтой уже существует");

        RoleEntity roleUser = roleService.getByName("ROLE_USER");
        Set<RoleEntity> roles = Set.of(roleUser);
        user.setRoles(roles);

        String password = passwordGenerator.generate();
        var encodedPassword = passwordEncoder.encode(password);
        user.setPasswordHash(encodedPassword);

        ClanEntity clan = clanService.getMinClan();
        user.setClan(clan);


        user.setActive(true);
        user.setClanPoints(0);
        user.setCoins(0);

        try {
            File file = new File("src/main/resources/passwords.txt");
            if (file.createNewFile()) {
                System.out.println("Файл создан");
            } else {
                System.out.println("Файл уже существует");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при создании файла");
            throw new RuntimeException(e);
        }

        try {
            FileWriter writer = new FileWriter("src/main/resources/passwords.txt");
            writer.write(user.getEmail() + " " + password + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл");
            throw new RuntimeException(e);
        }

        return userRepo.save(user);
    }

    public void setAdminPermission(UserEntity user) {
        RoleEntity adminRole = roleService.getByName(Roles.ADMIN);
        Set<RoleEntity> roles = user.getRoles();
        roles.add(adminRole);
        userRepo.save(user);
    }

    public Integer getPlace(Long id) {
        var users = userRepo.findAll();
        var usersList = users.stream().sorted(Comparator.comparingInt(UserEntity::getClanPoints).reversed()).toList();
        for (int i = 0; i < usersList.size(); i++) {
            if (usersList.get(i).getId().equals(id)) {
                return i + 1;
            }
        }
        return -1;
    }
}
