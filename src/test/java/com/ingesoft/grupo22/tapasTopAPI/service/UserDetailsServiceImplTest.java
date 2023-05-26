package com.ingesoft.grupo22.tapasTopAPI.service;

import com.ingesoft.grupo22.tapasTopAPI.entity.Usuario;
import com.ingesoft.grupo22.tapasTopAPI.repository.UsuarioRepository;
import com.ingesoft.grupo22.tapasTopAPI.security.UserPrincipal;
import com.ingesoft.grupo22.tapasTopAPI.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    public void loadUserByUsername(){
        String username = "username";

        Mockito.when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(new Usuario()));
        UserPrincipal userPrincipal = (UserPrincipal) userDetailsService.loadUserByUsername(username);
        Assertions.assertNotNull(userPrincipal);
    }

    @Test
    public void loadUserByUsernameException(){
        String username = "username";

        Mockito.when(usuarioRepository.findByUsername(username)).thenReturn(Optional.ofNullable(null));
        try{
            userDetailsService.loadUserByUsername(username);
        }catch(UsernameNotFoundException e){
            Assertions.assertEquals("User not found with username: " + username, e.getMessage());
        }
    }

}
