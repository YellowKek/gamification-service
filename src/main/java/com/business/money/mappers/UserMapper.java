package com.business.money.mappers;

import com.business.money.DTOs.user.CreateUserDTO;
import com.business.money.DTOs.user.UserResponseDTO;
import com.business.money.entities.domain.RoleEntity;
import com.business.money.entities.domain.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(CreateUserDTO createUserDTO);

    @Mapping(target = "clan", source = "clan.name")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapToUserRoles")
    UserResponseDTO toUserResponseDTO(UserEntity user);

    @Named("mapToUserRoles")
    default Set<String> mapToUserRoles(Set<RoleEntity> roles) {
        Set<String> res = new HashSet<>();
        for (RoleEntity role : roles) {
            res.add(role.getName());
        }
        return res;
    }
}
