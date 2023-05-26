package com.ingesoft.grupo22.tapasTopAPI.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class DegustacionDTO_IN {

    @NotBlank
    @NotEmpty
    private String plateName;
    private String type;
    private String description;
    private String photoUrl;
    private String originCountry;
    private String tasteQualifier;
    @NotBlank
    @NotEmpty
    private String localName;
}
