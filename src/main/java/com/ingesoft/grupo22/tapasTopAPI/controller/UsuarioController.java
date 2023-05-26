package com.ingesoft.grupo22.tapasTopAPI.controller;


import com.ingesoft.grupo22.tapasTopAPI.dto.*;
import com.ingesoft.grupo22.tapasTopAPI.entity.Usuario;
import com.ingesoft.grupo22.tapasTopAPI.exceptions.*;
import com.ingesoft.grupo22.tapasTopAPI.security.UserPrincipal;
import com.ingesoft.grupo22.tapasTopAPI.service.UsuarioService;
import com.ingesoft.grupo22.tapasTopAPI.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;

@RestController
@RequestMapping("api")
public class UsuarioController extends ExceptionHandling {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenUtils tokenUtils;

    @PostMapping("/registrar")
    public ResponseEntity<?> register(@Valid @RequestPart RegisterDTO_IN registerDTO_in,
                                      @RequestPart(required = false) MultipartFile image)
            throws EmailExistException, UsernameExistException {
        usuarioService.register(registerDTO_in, image);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/confirmarCuenta")
    public ResponseEntity<?> confirmAccount(@RequestBody String token) throws TokenNotValidException {
        usuarioService.confirmAccount(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@Valid @RequestBody LoginDTO_IN loginDTO_in){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO_in.getUsername(),
                loginDTO_in.getPassword()));
        HttpHeaders header  = new HttpHeaders();
        UserPrincipal userPrincipal = (UserPrincipal)userDetailsService.loadUserByUsername(loginDTO_in.getUsername());
        Usuario usuario = userPrincipal.getUsuario();
        String token = tokenUtils.createToken(usuario.getUsername(), usuario.getEmail());
        header.add("Authorization", "Bearer " + token);
        return new ResponseEntity<>(usuario.getId(),header,HttpStatus.OK);
    }

    @PostMapping("/recuperarContraseña")
    public ResponseEntity<?> enviarLinkRecuperarPwd(@RequestBody String email) throws UserNotFoundException {
        usuarioService.enviarCorreoRecuperarPwd(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/verificarToken")
    public ResponseEntity<?> verificarRecoveryToken(@RequestBody String token) throws TokenNotValidException {
        tokenUtils.verificarToken(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("usuarios/me/cambiarContraseña")
    public ResponseEntity<?> cambiarPwd(@RequestBody String password) throws UserNotFoundException {
        usuarioService.changePwd(getUsuarioLogeado(), password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/usuarios/me")
    public ResponseEntity<?> getUsuario() throws UserNotFoundException {
        return new ResponseEntity<>(usuarioService.getUsuario(getUsuarioLogeado()), HttpStatus.OK);
    }

    @PostMapping("/usuarios/me/degustaciones")
    public ResponseEntity<?> crearDegustacion(@Valid @RequestPart DegustacionDTO_IN degustacionDTO_in,
                                               @RequestPart(required = false) MultipartFile image)
            throws UserNotFoundException, LocalNotFoundException, DegustacionExistException {
        usuarioService.createDegustacion(getUsuarioLogeado(), degustacionDTO_in, image);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/usuarios/me/valoraciones")
    public ResponseEntity<?> valorarDegustacion(@Valid @RequestBody ValoracionDTO_IN valoracionDTO_in)
            throws UserNotFoundException, DegustacionNotFoundException, ValoracionExistException, LocalNotFoundException {
        usuarioService.rateDegustacion(getUsuarioLogeado(), valoracionDTO_in);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/usuarios/me/locales")
    public ResponseEntity<?> crearLocal(@Valid @RequestBody LocalDTO_IN localDTO_in) throws UserNotFoundException, LocalExistException {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        usuarioService.createLocal(username, localDTO_in);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
/*
    @PutMapping("/usuarios/me")
    public ResponseEntity<?> modificarUsuario(@Valid @RequestPart EditUsuarioDTO_IN editUsuarioDTO_in,
                                              @RequestPart(required = false) MultipartFile image) throws UserNotFoundException, EmailExistException, UsernameExistException {
        usuarioService.editUsuario(getUsuarioLogeado(), editUsuarioDTO_in, image);
        return new ResponseEntity<>(HttpStatus.OK);
    }
*/
    private String getUsuarioLogeado(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

}
