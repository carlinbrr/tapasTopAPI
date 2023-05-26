package com.ingesoft.grupo22.tapasTopAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class UsuarioDTO_OUT {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private String country;
    private String description;
    private String photoUrl;
    private List<DegustacionDTO_OUT> degustacionList;
    private int nDegustacionesLastWeek;
    private List<LocalDTO_OUT> localList;
    private int nLocalesLastWeek;
    private List<DegustacionDTO_OUT> degustacionFavList;
}
