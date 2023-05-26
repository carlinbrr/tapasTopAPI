package com.ingesoft.grupo22.tapasTopAPI.utils;

import com.ingesoft.grupo22.tapasTopAPI.dto.DegustacionDTO_OUT;
import com.ingesoft.grupo22.tapasTopAPI.dto.LocalDTO_OUT;
import com.ingesoft.grupo22.tapasTopAPI.dto.UsuarioDTO_OUT;
import com.ingesoft.grupo22.tapasTopAPI.entity.Usuario;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.ingesoft.grupo22.tapasTopAPI.utils.DegustacionMapper.mapToDegustacionDto;

public class UsuarioMapper {
    public static UsuarioDTO_OUT mapToUsuarioDto(Usuario usuario){
        List<DegustacionDTO_OUT> degustacionDtoList = usuario.getDegustacionList().stream().
                map(DegustacionMapper::mapToDegustacionDto).collect(Collectors.toList());

        List<LocalDTO_OUT> localDtoList = usuario.getLocalesAdded().stream().
                map(LocalMapper::mapToLocalDto).collect(Collectors.toList());

        int nDegustacionesLastWeek = (int) degustacionDtoList.stream().filter(d -> {
            Date weekBefore = new Date(new Date().getTime() - 604800000);
            return d.getCreatedAt().after(weekBefore);
        }).count();

        int nLocalesLastWeek = (int) localDtoList.stream().filter(d -> {
            Date weekBefore = new Date(new Date().getTime() - 604800000);
            return d.getCreatedAt().after(weekBefore);
        }).count();

        List<DegustacionDTO_OUT> degustacionesFavList = usuario.getUsuarioValoraDegustacionList().stream()
                .filter(v -> (v.getRate()>=3)).map(v -> mapToDegustacionDto(v.getDegustacion())).collect(Collectors.toList());

        return new UsuarioDTO_OUT(usuario.getUsername(), usuario.getEmail(), usuario.getFirstName(),
                usuario.getLastName(), usuario.getAddress(), usuario.getGender(), usuario.getCountry(),
                usuario.getDescription(), usuario.getPhotoUrl(), degustacionDtoList, nDegustacionesLastWeek,
                localDtoList, nLocalesLastWeek, degustacionesFavList);
    }
}
