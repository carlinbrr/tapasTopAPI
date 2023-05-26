package com.ingesoft.grupo22.tapasTopAPI.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ValoracionDTO_IN {

    @NotEmpty
    @NotBlank
    private String plateName;
    @NotEmpty
    @NotBlank
    private String localName;
    @NotNull
    private int rate;
}
