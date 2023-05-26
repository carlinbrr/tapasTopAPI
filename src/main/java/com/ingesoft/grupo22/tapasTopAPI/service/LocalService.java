package com.ingesoft.grupo22.tapasTopAPI.service;

import com.ingesoft.grupo22.tapasTopAPI.dto.DegustacionDTO_OUT;
import com.ingesoft.grupo22.tapasTopAPI.exceptions.DegustacionNotFoundException;
import com.ingesoft.grupo22.tapasTopAPI.exceptions.LocalNotFoundException;


public interface LocalService {
    DegustacionDTO_OUT findDegustacion(String localName, String plateName) throws LocalNotFoundException, DegustacionNotFoundException;
}
