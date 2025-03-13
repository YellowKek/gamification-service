package com.business.money.DTOs.clan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClanCreateDTO {
    @JsonProperty("name")
    private String name;
}
