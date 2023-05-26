package com.ingesoft.grupo22.tapasTopAPI.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
public class Degustacion {

    public Degustacion(String plateName, String description, String photoURL, String type, String originCountry,
                       String tasteQualifier, Date createdAt, Usuario usuario, Local local) {
        this.plateName = plateName;
        this.description = description;
        this.photoURL = photoURL;
        this.type = type;
        this.originCountry = originCountry;
        this.tasteQualifier = tasteQualifier;
        this.createdAt = createdAt;
        this.usuario = usuario;
        this.local = local;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String plateName;
    private String description;
    private String photoURL;
    private String type;
    private String originCountry;
    private String tasteQualifier;
    private Date createdAt;
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_local")
    private Local local;

    @OneToMany(mappedBy = "degustacion")
    private List<UsuarioValoraDegustacion> usuarioValoraDegustacionList = new ArrayList<>();
}
