package com.business.money.DTOs.clan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClanResponseDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("points_amount")
    private Integer pointsAmount;

    @JsonProperty("image")
    private String image;

    @JsonProperty("place")
    private Integer place;

    @JsonProperty("members_count")
    private Integer membersCount;
}
