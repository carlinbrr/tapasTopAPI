package com.ingesoft.grupo22.tapasTopAPI.service;

import com.ingesoft.grupo22.tapasTopAPI.dto.*;
import com.ingesoft.grupo22.tapasTopAPI.exceptions.*;
import org.springframework.web.multipart.MultipartFile;


public interface UsuarioService {
    void register(RegisterDTO_IN registerDTO_in, MultipartFile image) throws UsernameExistException, EmailExistException;
    void confirmAccount(String token) throws TokenNotValidException;
    UsuarioDTO_OUT getUsuario(String username) throws UserNotFoundException;
    void changePwd(String username, String password) throws UserNotFoundException;
    void enviarCorreoRecuperarPwd(String email) throws UserNotFoundException;
    void createDegustacion(String username, DegustacionDTO_IN degustacionDTO_in, MultipartFile image)
            throws UserNotFoundException, DegustacionExistException, LocalNotFoundException;
    void rateDegustacion(String usuarioLogeado, ValoracionDTO_IN valoracionDTO_in) throws UserNotFoundException, DegustacionNotFoundException, ValoracionExistException, LocalNotFoundException;
    void createLocal(String username, LocalDTO_IN localDTO_in) throws UserNotFoundException, LocalExistException;
    //void editUsuario(String usuarioLogeado, EditUsuarioDTO_IN editUsuarioDTO_in, MultipartFile image) throws UsernameExistException, EmailExistException, UserNotFoundException;
}
