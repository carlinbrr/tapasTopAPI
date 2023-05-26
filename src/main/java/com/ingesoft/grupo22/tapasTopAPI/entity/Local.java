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
public class Local {

    public Local(String name, String address, Double latitude, Double longitude, Date createdAt, Usuario usuarioCreator) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = createdAt;
        this.usuarioCreator = usuarioCreator;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private Date createdAt;
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuarioCreator;
    @OneToMany(mappedBy = "local", cascade = CascadeType.ALL)
    private List<Degustacion> degustacionList = new ArrayList<>();
}


