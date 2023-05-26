package com.ingesoft.grupo22.tapasTopAPI.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RegisterDTO_IN {
    @NotBlank
    @NotEmpty
    private String username;
    @NotNull @Email
    private String email;
    @NotBlank
    @NotEmpty
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String description;
    private String photoUrl;
    private String gender;
    private String country;
}
