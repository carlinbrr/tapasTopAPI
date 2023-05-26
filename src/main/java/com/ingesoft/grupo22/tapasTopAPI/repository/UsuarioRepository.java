package com.ingesoft.grupo22.tapasTopAPI.repository;

import com.ingesoft.grupo22.tapasTopAPI.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername (String username);
    Optional<Usuario> findByEmail(String email);
}
