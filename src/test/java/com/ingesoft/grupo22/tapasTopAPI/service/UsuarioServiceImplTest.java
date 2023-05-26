package com.ingesoft.grupo22.tapasTopAPI.service;


import com.ingesoft.grupo22.tapasTopAPI.dto.*;
import com.ingesoft.grupo22.tapasTopAPI.entity.Degustacion;
import com.ingesoft.grupo22.tapasTopAPI.entity.Local;
import com.ingesoft.grupo22.tapasTopAPI.entity.Usuario;
import com.ingesoft.grupo22.tapasTopAPI.entity.UsuarioValoraDegustacion;
import com.ingesoft.grupo22.tapasTopAPI.exceptions.*;
import com.ingesoft.grupo22.tapasTopAPI.repository.DegustacionRepository;
import com.ingesoft.grupo22.tapasTopAPI.repository.LocalRepository;
import com.ingesoft.grupo22.tapasTopAPI.repository.UsuarioRepository;
import com.ingesoft.grupo22.tapasTopAPI.repository.UsuarioValoraDegustacionRepository;
import com.ingesoft.grupo22.tapasTopAPI.service.impl.UsuarioServiceImpl;
import com.ingesoft.grupo22.tapasTopAPI.utils.TokenUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplTest {

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TokenUtils tokenUtils;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private DegustacionRepository degustacionRepository;

    @Mock
    private UsuarioValoraDegustacionRepository usuarioValoraDegustacionRepository;

    @Mock
    private LocalRepository localRepository;

    @Test
    public void register() throws EmailExistException, UsernameExistException {
        RegisterDTO_IN registerDto = new RegisterDTO_IN();
        registerDto.setEmail("user@gmail.com");
        registerDto.setUsername("username");
        registerDto.setPassword("12345");
        MultipartFile image = null;

        Mockito.when(usuarioRepository.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(null));
        Mockito.when(usuarioRepository.findByUsername(Mockito.any())).thenReturn(Optional.ofNullable(null));
        Mockito.when(bCryptPasswordEncoder.encode(Mockito.any())).thenReturn("abvd123");
        usuarioService.register(registerDto, image);
    }

    @Test
    public void registerException(){
        RegisterDTO_IN registerDto = new RegisterDTO_IN();
        registerDto.setEmail("user@gmail.com");
        registerDto.setUsername("username");
        registerDto.setPassword("12345");
        MultipartFile image = null;

        Mockito.when(usuarioRepository.findByUsername(Mockito.any())).
                thenReturn(Optional.of(new Usuario()));
        try{
            usuarioService.register(registerDto, image);
        }catch(UsernameExistException | EmailExistException e){
            Assertions.assertEquals("Usuario existente con username: " + registerDto.getUsername(),
                    e.getMessage());
        }
    }

    @Test
    public void registerException2(){
        RegisterDTO_IN registerDto = new RegisterDTO_IN();
        registerDto.setEmail("user@gmail.com");
        registerDto.setUsername("username");
        registerDto.setPassword("12345");
        MultipartFile image = null;

        Mockito.when(usuarioRepository.findByEmail(Mockito.any())).
                thenReturn(Optional.of(new Usuario()));
        try{
            usuarioService.register(registerDto, image);
        }catch(UsernameExistException | EmailExistException e){
            Assertions.assertEquals("Usuario existente con email: " + registerDto.getEmail(),
                    e.getMessage());
        }
    }

    @Test
    public void getUsuario() throws UserNotFoundException {
        String username = "username";

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("username");
        usuario.setDegustacionList(new ArrayList<>());
        Mockito.when(usuarioRepository.findByUsername("username")).thenReturn(Optional.of(usuario));
        UsuarioDTO_OUT usuarioDTO_out = usuarioService.getUsuario(username);
        Assertions.assertEquals("username", usuarioDTO_out.getUsername());
    }

    @Test
    public void confirmAccount() throws TokenNotValidException {
        String token = "123ABC";

        Usuario usuario = new Usuario();
        usuario.setUsername("username");
        usuario.setEnabled(false);

        Mockito.when(tokenUtils.verificarToken(token)).thenReturn("username");
        Mockito.when(usuarioRepository.findByUsername("username")).thenReturn(Optional.of(usuario));
        usuarioService.confirmAccount(token);
        Assertions.assertEquals(true, usuario.isEnabled());
    }

    @Test
    public void enviarCorreoRecuperarPwd() throws UserNotFoundException {
        String email = "user@gmail.com";

        Usuario usuario = new Usuario();
        usuario.setUsername("username");
        Mockito.when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        usuarioService.enviarCorreoRecuperarPwd(email);
    }

    @Test
    public void enviarCorreoRecuperarPwdException() throws UserNotFoundException {
        String email = "user@gmail.com";

        Mockito.when(usuarioRepository.findByEmail(email)).thenReturn(Optional.ofNullable(null));
        try{
            usuarioService.enviarCorreoRecuperarPwd(email);
        }catch (UserNotFoundException e){
            Assertions.assertEquals("No existe ningun usuario registrado con el email: " + email,
                    e.getMessage());
        }
    }

    @Test
    public void changePwd() throws UserNotFoundException {
        String username = "username";
        String pwdIn = "password";

        Usuario usuario = new Usuario();
        usuario.setUsername("username");
        usuario.setPassword("abc");

        Mockito.when(usuarioRepository.findByUsername("username")).thenReturn(Optional.of(usuario));
        Mockito.when(bCryptPasswordEncoder.encode(pwdIn)).thenReturn("1234ABCD");
        usuarioService.changePwd(username, pwdIn);
        Assertions.assertEquals("1234ABCD", usuario.getPassword());
    }

    @Test
    public void createDegustacion() throws UserNotFoundException, LocalNotFoundException, DegustacionExistException {
        String username = "username";
        DegustacionDTO_IN degustacionDTO_in = new DegustacionDTO_IN();
        degustacionDTO_in.setPlateName("paella");
        degustacionDTO_in.setLocalName("local1");
        MultipartFile image = null;

        Usuario usuario = new Usuario();
        Local local = new Local();
        local.setDegustacionList(new ArrayList<>());
        Mockito.when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(usuario));
        Mockito.when(localRepository.findByNameIgnoreCase("local1")).thenReturn(Optional.of(local));
        usuarioService.createDegustacion(username, degustacionDTO_in, image);
    }

    @Test
    public void createDegustacionException(){
        String username = "username";
        DegustacionDTO_IN degustacionDTO_in = new DegustacionDTO_IN();
        degustacionDTO_in.setPlateName("paella");
        degustacionDTO_in.setLocalName("local1");
        MultipartFile image = null;

        Usuario usuario = new Usuario();
        Degustacion degustacion = new Degustacion();
        degustacion.setPlateName("paella");
        Local local = new Local();
        local.setDegustacionList(List.of(degustacion));
        Mockito.when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(usuario));
        Mockito.when(localRepository.findByNameIgnoreCase("local1")).thenReturn(Optional.of(local));
        try{
            usuarioService.createDegustacion(username, degustacionDTO_in, image);
        }catch (DegustacionExistException | UserNotFoundException | LocalNotFoundException e){
            Assertions.assertEquals("La degustación con nombre de plato/tapa: "+ degustacionDTO_in.getPlateName()
                    + " ya existe para este local",e.getMessage());
        }
    }

    @Test
    public void rateDegustacion() throws UserNotFoundException, DegustacionNotFoundException, LocalNotFoundException, ValoracionExistException {
        String username = "username";
        ValoracionDTO_IN valoracionDTO_in = new ValoracionDTO_IN();
        valoracionDTO_in.setLocalName("local1");
        valoracionDTO_in.setPlateName("plato1");
        valoracionDTO_in.setRate(4);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Degustacion degustacion = new Degustacion();
        degustacion.setPlateName("plato1");
        degustacion.setUsuarioValoraDegustacionList(new ArrayList<>());

        Local local = new Local();
        local.setName("local1");
        local.setDegustacionList(List.of(degustacion));

        Mockito.when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(usuario));
        Mockito.when(localRepository.findByNameIgnoreCase("local1")).thenReturn(Optional.of(local));

        usuarioService.rateDegustacion(username, valoracionDTO_in);
    }

    @Test
    public void rateDegustacionException() throws UserNotFoundException, DegustacionNotFoundException, ValoracionExistException {
        String username = "username";
        ValoracionDTO_IN valoracionDTO_in = new ValoracionDTO_IN();
        valoracionDTO_in.setLocalName("local1");
        valoracionDTO_in.setPlateName("plato1");
        valoracionDTO_in.setRate(4);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        UsuarioValoraDegustacion valoracion = new UsuarioValoraDegustacion();
        valoracion.setUsuario(usuario);

        Degustacion degustacion = new Degustacion();
        degustacion.setPlateName("plato1");
        degustacion.setUsuarioValoraDegustacionList(List.of(valoracion));

        Local local = new Local();
        local.setName("local1");
        local.setDegustacionList(List.of(degustacion));

        Mockito.when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(usuario));
        Mockito.when(localRepository.findByNameIgnoreCase("local1")).thenReturn(Optional.of(local));

        try{
            usuarioService.rateDegustacion(username, valoracionDTO_in);
        }catch(ValoracionExistException e){
            Assertions.assertEquals("El usuario ya ha valorado previemante esta degustación",
                    e.getMessage());
        } catch (LocalNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createLocal() throws UserNotFoundException, LocalExistException {
        String username = "username";
        LocalDTO_IN localDTO_in = new LocalDTO_IN();
        localDTO_in.setName("local1");

        Mockito.when(usuarioRepository.findByUsername("username")).thenReturn(Optional.of(new Usuario()));
        Mockito.when(localRepository.findByNameIgnoreCase("local1")).thenReturn(Optional.ofNullable(null));

        usuarioService.createLocal(username, localDTO_in);
    }

    @Test
    public void createLocalException(){
        String username = "username";
        LocalDTO_IN localDTO_in = new LocalDTO_IN();
        localDTO_in.setName("local1");

        Mockito.when(usuarioRepository.findByUsername("username")).thenReturn(Optional.of(new Usuario()));
        Mockito.when(localRepository.findByNameIgnoreCase("local1")).thenReturn(Optional.of(new Local()));
        try{
            usuarioService.createLocal(username, localDTO_in);
        }catch(LocalExistException | UserNotFoundException e){
            Assertions.assertEquals("El local con nombre " + localDTO_in.getName() + " ya existe",
                    e.getMessage());
        }
    }

}
