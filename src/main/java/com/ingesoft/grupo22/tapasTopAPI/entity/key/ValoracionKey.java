package com.ingesoft.grupo22.tapasTopAPI.entity.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValoracionKey implements Serializable {

    @Column(name = "id_usuario")
    private Long idUsuario;
    @Column(name = "id_degustacion")
    private Long idDegustacion;
}
