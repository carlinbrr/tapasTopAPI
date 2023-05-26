package com.ingesoft.grupo22.tapasTopAPI.utils;

import com.ingesoft.grupo22.tapasTopAPI.dto.LocalDTO_OUT;
import com.ingesoft.grupo22.tapasTopAPI.entity.Local;

public class LocalMapper {

    public static LocalDTO_OUT mapToLocalDto(Local local){
        return new LocalDTO_OUT(local.getId(), local.getName(), local.getAddress(), local.getLatitude(), local.getLongitude(), local.getCreatedAt());
    }
}
