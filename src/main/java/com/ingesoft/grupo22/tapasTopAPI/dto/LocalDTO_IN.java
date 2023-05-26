package com.ingesoft.grupo22.tapasTopAPI.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class LocalDTO_IN {

    @NotBlank
    @NotEmpty
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
}
