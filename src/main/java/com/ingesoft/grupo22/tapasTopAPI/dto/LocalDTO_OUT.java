package com.ingesoft.grupo22.tapasTopAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class LocalDTO_OUT {

    private Long id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private Date createdAt;
}
