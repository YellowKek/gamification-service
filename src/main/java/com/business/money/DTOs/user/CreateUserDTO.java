package com.business.money.DTOs.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDTO {
    @NotBlank(message = "имя не должно быть пустым")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "фамилия не должна быть пустой")
    @JsonProperty(value = "surname")
    private String surname;

    @JsonProperty("patronymic")
    private String patronymic;

    @Email
    @NotBlank(message = "почта не должна быть пустой")
    @JsonProperty("email")
    private String email;
}
