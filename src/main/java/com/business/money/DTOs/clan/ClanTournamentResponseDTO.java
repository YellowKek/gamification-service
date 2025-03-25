package com.business.money.DTOs.clan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClanTournamentResponseDTO {
    @JsonProperty("name")
    private String name;

    @JsonProperty("points_amount")
    private Integer pointsAmount;

    @JsonProperty("members_count")
    private Integer membersCount;

    @JsonProperty("best_member_name")
    private String bestMemberName;

    @JsonProperty("best_member_email")
    private String bestMemberEmail;

    @JsonProperty("image")
    private String image;

}
