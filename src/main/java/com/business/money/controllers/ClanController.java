package com.business.money.controllers;

import com.business.money.DTOs.clan.ClanCreateDTO;
import com.business.money.DTOs.clan.ClanResponseDTO;
import com.business.money.DTOs.clan.ClanTournamentResponseDTO;
import com.business.money.DTOs.user.UserResponseDTO;
import com.business.money.entities.domain.ClanEntity;
import com.business.money.entities.security.AdminPermission;
import com.business.money.entities.security.UserPermission;
import com.business.money.exception.exceptions.NotFoundException;
import com.business.money.mappers.UserMapper;
import com.business.money.services.ClanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
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

        ClanEntity clanEntity = new ClanEntity();
        clanEntity.setName(name);
        clanEntity.setPointsAmount(0);

        // Если изображение передано в формате Base64, декодируем его в байты и сохраняем в БД
        if (clanCreateDTO.getImage() != null && !clanCreateDTO.getImage().isEmpty()) {
            byte[] imageBytes = Base64.getDecoder().decode(clanCreateDTO.getImage());
            clanEntity.setImage(imageBytes);  // Сохраняем изображение в БД
        }

        System.out.println(clanEntity);

        return clanService.save(clanEntity);  // Сохраняем клан в БД
    }

    @GetMapping("withMembers")
    @UserPermission
    public List<ClanResponseDTO> getClansWithMembersCount() throws NotFoundException {
        return clanService.getAllClanMembersCount();
    }

    @GetMapping("/tournament")
    @UserPermission
    public List<ClanTournamentResponseDTO> getTournaments() {
        List<ClanTournamentResponseDTO> response = new ArrayList<>();
        var clans = clanService.getAllClans();
        for (var clan : clans) {
            var bestMember = clanService.getBestMember(clan.getName());

            String image = convertToBase64(clan.getImage());

            response.add(new ClanTournamentResponseDTO(
                    clan.getName(),
                    clan.getPointsAmount(),
                    clan.getMembers().size(),
                    bestMember == null ? "" : bestMember.getName(),
                    bestMember == null ? "" : bestMember.getEmail(),
                    image));
        }
        return response;
    }

    @GetMapping("/{name}")
    @UserPermission
    public ResponseEntity<ClanResponseDTO> getClanByName(@PathVariable String name) throws NotFoundException {
        ClanEntity clan = clanService.findByName(name);
        if (clan == null) {
            return ResponseEntity.notFound().build(); // 404
        }

        Integer place = clanService.getClanPlace(clan.getName());

        String imageBase64 = convertToBase64(clan.getImage());
        var membersCount = clanService.getClanMembersCount(clan.getId());
        ClanResponseDTO response = new ClanResponseDTO(
                clan.getId(),
                clan.getName(),
                clan.getPointsAmount(),
                imageBase64,
                place,
                membersCount
        );

        return ResponseEntity.ok(response); // 200
    }

    private String convertToBase64(byte[] image) {
        String base64Image = null;
        if (image != null) {
            base64Image = Base64.getEncoder().encodeToString(image);
        }
        return base64Image;
    }

}
