package com.ingesoft.grupo22.tapasTopAPI.repository;

import com.ingesoft.grupo22.tapasTopAPI.entity.Degustacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DegustacionRepository extends JpaRepository<Degustacion, Long> {
}
