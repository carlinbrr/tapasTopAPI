package com.ingesoft.grupo22.tapasTopAPI.entity;

import com.ingesoft.grupo22.tapasTopAPI.entity.key.ValoracionKey;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table
@NoArgsConstructor
public class UsuarioValoraDegustacion {

    public UsuarioValoraDegustacion(ValoracionKey id, int rate, Usuario usuario, Degustacion degustacion) {
        this.id = id;
        this.rate = rate;
        this.usuario = usuario;
        this.degustacion = degustacion;
    }

    @EmbeddedId
    private ValoracionKey id;
    private int rate;

    @ManyToOne
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @MapsId("idDegustacion")
    @JoinColumn(name= "id_degustacion")
    private Degustacion degustacion;

}
