package com.ingesoft.grupo22.tapasTopAPI.repository;

import com.ingesoft.grupo22.tapasTopAPI.entity.UsuarioValoraDegustacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioValoraDegustacionRepository extends JpaRepository<UsuarioValoraDegustacion, Long> {
}
