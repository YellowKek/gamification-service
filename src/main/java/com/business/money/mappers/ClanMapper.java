package com.business.money.mappers;

import com.business.money.DTOs.clan.ClanResponseDTO;
import com.business.money.entities.domain.ClanEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Base64;

@Mapper(componentModel = "Spring")
public interface ClanMapper {

    @Mapping(target = "image", source = "image", qualifiedByName = "mapToString")
    ClanResponseDTO clanToClanResponseDTO(ClanEntity clanEntity);

    @Named("mapToString")
    default String mapImage(byte[] image) {
        if (image == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(image);
    }
}
