package com.oppa.project.payment_system.api.model.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RegistrationBody {
    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 6)
    @Size(max = 32)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$")
    private String password;

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    private List<String> roles;

}
