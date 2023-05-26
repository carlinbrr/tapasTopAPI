package com.ingesoft.grupo22.tapasTopAPI.utils;

import com.ingesoft.grupo22.tapasTopAPI.dto.DegustacionDTO_OUT;
import com.ingesoft.grupo22.tapasTopAPI.entity.Degustacion;
import com.ingesoft.grupo22.tapasTopAPI.entity.UsuarioValoraDegustacion;

import java.util.List;

public class DegustacionMapper {

    public static DegustacionDTO_OUT mapToDegustacionDto(Degustacion degustacion){
        List<UsuarioValoraDegustacion> usuarios = degustacion.getUsuarioValoraDegustacionList();
        double rateMedia = 0.0;
        if(!usuarios.isEmpty()){
            double rate = 0.0;
            for(UsuarioValoraDegustacion u : usuarios) rate += u.getRate();
            rateMedia = Math.round(rate/usuarios.size()*100.0)/100.0;
        }
        return new DegustacionDTO_OUT(degustacion.getId(), degustacion.getPlateName(),degustacion.getType(),degustacion.getDescription(),
                degustacion.getPhotoURL(),degustacion.getOriginCountry(), degustacion.getTasteQualifier(),
                degustacion.getCreatedAt(), rateMedia);
    }
}
