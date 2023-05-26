package com.ingesoft.grupo22.tapasTopAPI.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
public class Usuario {

    public Usuario(String username, String email, String password, String firstName, String lastName,
                   String address, String description, String photoUrl, String gender, String country,
                   boolean enabled) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.description = description;
        this.photoUrl = photoUrl;
        this.gender = gender;
        this.country = country;
        this.enabled = enabled;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String description;
    private String photoUrl;
    private String gender;
    private String country;
    private boolean enabled;

    @OneToMany(mappedBy = "usuario", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    private List<Degustacion> degustacionList = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    private List<UsuarioValoraDegustacion> usuarioValoraDegustacionList = new ArrayList<>();

    @OneToMany(mappedBy = "usuarioCreator")
    private List<Local> localesAdded = new ArrayList<>();
}
