package com.business.money.DTOs.clan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ClanMembersCountResponseDTO {
    @JsonProperty("name")
    private String name;

    @JsonProperty("pointsAmount")
    private Integer points_amount;

    @JsonProperty("membersCount")
    private Integer membersCount;
}
