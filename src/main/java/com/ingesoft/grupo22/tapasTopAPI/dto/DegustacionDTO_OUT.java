package com.ingesoft.grupo22.tapasTopAPI.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
public class DegustacionDTO_OUT {

    private Long id;
    private String plateName;
    private String type;
    private String description;
    private String photoUrl;
    private String originCountry;
    private String tasteQualifier;
    private Date createdAt;
    private double rateMedia;
}
