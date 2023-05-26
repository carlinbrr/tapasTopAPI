package com.ingesoft.grupo22.tapasTopAPI.repository;

import com.ingesoft.grupo22.tapasTopAPI.entity.Local;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocalRepository extends JpaRepository<Local, Long> {
    Optional<Local> findByNameIgnoreCase (String name);

}
