package com.business.money.DTOs.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserResponseDTO {
 
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("patronymic")
    private String patronymic;

    @JsonProperty("email")
    private String email;

    @JsonProperty("clan_points")
    private Integer clanPoints;

    @JsonProperty("coins")
    private Integer coins;

    @JsonProperty("clan")
    private String clan;

    @JsonProperty("is_active")
    private boolean active;

    @JsonProperty("place")
    private Integer place;

    @JsonProperty("roles")
    private Set<String> roles;
}
