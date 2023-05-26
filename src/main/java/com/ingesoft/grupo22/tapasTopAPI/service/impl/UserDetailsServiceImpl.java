package com.ingesoft.grupo22.tapasTopAPI.service.impl;

import com.ingesoft.grupo22.tapasTopAPI.entity.Usuario;
import com.ingesoft.grupo22.tapasTopAPI.repository.UsuarioRepository;
import com.ingesoft.grupo22.tapasTopAPI.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new UserPrincipal(usuario);
    }
}
