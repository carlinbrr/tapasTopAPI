package com.ingesoft.grupo22.tapasTopAPI.service.impl;

import com.ingesoft.grupo22.tapasTopAPI.dto.DegustacionDTO_OUT;
import com.ingesoft.grupo22.tapasTopAPI.entity.Degustacion;
import com.ingesoft.grupo22.tapasTopAPI.entity.Local;
import com.ingesoft.grupo22.tapasTopAPI.exceptions.DegustacionNotFoundException;
import com.ingesoft.grupo22.tapasTopAPI.exceptions.LocalNotFoundException;
import com.ingesoft.grupo22.tapasTopAPI.repository.DegustacionRepository;
import com.ingesoft.grupo22.tapasTopAPI.repository.LocalRepository;
import com.ingesoft.grupo22.tapasTopAPI.service.LocalService;
import static com.ingesoft.grupo22.tapasTopAPI.utils.DegustacionMapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocalServiceImpl implements LocalService {

    @Autowired
    private LocalRepository localRepository;

    @Override
    public DegustacionDTO_OUT findDegustacion(String localName, String plateName) throws LocalNotFoundException, DegustacionNotFoundException {
        Local local = localRepository.findByNameIgnoreCase(localName).orElseThrow(() -> new LocalNotFoundException("El local con nombre: " +
                localName + " no ha sido encontrado"));
        Optional<Degustacion> degustacion =  local.getDegustacionList().stream().filter(d -> d.getPlateName().equalsIgnoreCase(plateName)).findFirst();
        if(!degustacion.isPresent()) throw new DegustacionNotFoundException("La degustacion con nombre de plato/tapa: " + plateName + " no existe");
        return mapToDegustacionDto(degustacion.get());
    }
}
