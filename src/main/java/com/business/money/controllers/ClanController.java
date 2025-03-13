package com.business.money.controllers;

import com.business.money.DTOs.clan.ClanCreateDTO;
import com.business.money.DTOs.clan.ClanMembersCountResponseDTO;
import com.business.money.DTOs.user.UserResponseDTO;
import com.business.money.entities.domain.ClanEntity;
import com.business.money.entities.security.AdminPermission;
import com.business.money.entities.security.UserPermission;
import com.business.money.exception.exceptions.NotFoundException;
import com.business.money.mappers.UserMapper;
import com.business.money.services.ClanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/clan")
@RequiredArgsConstructor
public class ClanController {
    private final ClanService clanService;
    private final UserMapper userMapper;

    @GetMapping("/members")
    @UserPermission
    public List<UserResponseDTO> getMembers(@RequestParam Long id) throws NotFoundException {
        return clanService.getMembers(id).stream().map(userMapper::toUserResponseDTO).toList();
    }

    @PostMapping
    @AdminPermission
    public ClanEntity createClan(@RequestBody ClanCreateDTO clanCreateDTO) throws NotFoundException {
        System.out.println("clan creation");
        String name = clanCreateDTO.getName();
        if (clanService.findByName(name) != null) {
            throw new NotFoundException("Clan with name " + name + " already exists");
        }
        System.out.println(name);
        ClanEntity clanEntity = new ClanEntity();
        clanEntity.setName(name);
        clanEntity.setPointsAmount(0);
        System.out.println(clanEntity);

        return clanService.save(clanEntity);
    }

    @GetMapping
    @UserPermission
    public List<ClanMembersCountResponseDTO> getClansWithMembersCount() throws NotFoundException {
        List<ClanEntity> clans = clanService.getAllClans();
        List<ClanMembersCountResponseDTO> response = new ArrayList<>();
        for (ClanEntity clan : clans) {
            var membersCount = clanService.getMembers(clan.getId()).size();
            response.add(new ClanMembersCountResponseDTO(clan.getName(), clan.getPointsAmount(), membersCount));
        }
        return response;
    }

}
