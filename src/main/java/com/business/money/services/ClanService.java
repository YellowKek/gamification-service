package com.business.money.services;

import com.business.money.DTOs.clan.ClanResponseDTO;
import com.business.money.entities.domain.ClanEntity;
import com.business.money.entities.domain.UserEntity;
import com.business.money.exception.exceptions.NotFoundException;
import com.business.money.mappers.ClanMapper;
import com.business.money.repos.ClanRepo;
import com.business.money.util.ClanComparator;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ClanService {
    private final ClanRepo clanRepo;
    private final ClanMapper clanMapper;

    @Transactional(readOnly = true)
    public ClanEntity findByName(String name) {
        return clanRepo.findByName(name).orElse(null);
    }

    public ClanEntity findById(Long id) throws NotFoundException {
        return clanRepo.findById(id).orElseThrow(() -> new NotFoundException("Клана с таким id не существует"));
    }

    public Set<UserEntity> getMembers(Long id) throws NotFoundException {
        ClanEntity clan = findById(id);
        return clan.getMembers();
    }

    public List<ClanEntity> getAllClans() {
        return clanRepo.findAll();
    }

    // возварщает клан с миниальным кол-вом участников
    public ClanEntity getMinClan() {
        return getAllClans().stream().min(ClanComparator::compare).get();
    }

    @Transactional
    public ClanEntity save(ClanEntity clan) {
        return clanRepo.save(clan);
    }

    public Integer getClanMembersCount(Long id) throws NotFoundException {
        return getMembers(id).size();
    }

    public List<ClanResponseDTO> getAllClanMembersCount() throws NotFoundException {
        var clans = getAllClans();
        var result = new ArrayList<ClanResponseDTO>();
        for (var clan : clans) {
            var temp = clanMapper.clanToClanResponseDTO(clan);
            temp.setMembersCount(getClanMembersCount(clan.getId()));
            result.add(temp);
        }
        return result;
    }

    @Transactional
    public UserEntity getBestMember(String clanName) {
        var clan = findByName(clanName);
        var members = clan.getMembers();
        return members.stream().max(Comparator.comparingInt(UserEntity::getClanPoints)).orElse(null);
    }

    public byte[] getImage(Long id) throws NotFoundException {
        ClanEntity clan = findById(id);
        return clan.getImage();
    }

    @Transactional(readOnly = true)
    public Integer getClanPlace(String name) {
        var clans = getAllClans();
        var sortedClans = clans.stream().sorted(Comparator.comparingInt(ClanEntity::getPointsAmount).reversed()).toList();
        for (int i = 0; i < sortedClans.size(); i++) {
            if (sortedClans.get(i).getName().equals(name)) {
                return i + 1;
            }
        }
        return null;
    }
}
