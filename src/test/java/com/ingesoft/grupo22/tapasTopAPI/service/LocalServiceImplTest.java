package com.ingesoft.grupo22.tapasTopAPI.service;

import com.ingesoft.grupo22.tapasTopAPI.entity.Degustacion;
import com.ingesoft.grupo22.tapasTopAPI.entity.Local;
import com.ingesoft.grupo22.tapasTopAPI.exceptions.DegustacionNotFoundException;
import com.ingesoft.grupo22.tapasTopAPI.exceptions.LocalNotFoundException;
import com.ingesoft.grupo22.tapasTopAPI.repository.LocalRepository;
import com.ingesoft.grupo22.tapasTopAPI.service.impl.LocalServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LocalServiceImplTest {

    @InjectMocks
    private LocalServiceImpl localService;

    @Mock
    private LocalRepository localRepository;

    @Test
    public void findDegustacion() throws DegustacionNotFoundException, LocalNotFoundException {
        String localName = "local1";
        String plateName = "plato1";

        Degustacion degustacion = new Degustacion();
        degustacion.setPlateName("plato1");

        Local local = new Local();
        local.setName("local1");
        local.setDegustacionList(List.of(degustacion));

        Mockito.when(localRepository.findByNameIgnoreCase("local1")).thenReturn(Optional.of(local));
        localService.findDegustacion(localName, plateName);
    }

    @Test
    public void findDegustacionException() {
        String localName = "local1";
        String plateName = "plato1";

        Local local = new Local();
        local.setName("local1");
        local.setDegustacionList(new ArrayList<>());

        Mockito.when(localRepository.findByNameIgnoreCase("local1")).thenReturn(Optional.of(local));

        try{
            localService.findDegustacion(localName, plateName);
        }catch(DegustacionNotFoundException | LocalNotFoundException e){
            Assertions.assertEquals("La degustacion con nombre de plato/tapa: " + plateName + " no existe",
                    e.getMessage());
        }
    }


}
