package com.ingesoft.grupo22.tapasTopAPI.service.impl;

import com.ingesoft.grupo22.tapasTopAPI.dto.*;
import com.ingesoft.grupo22.tapasTopAPI.entity.*;
import com.ingesoft.grupo22.tapasTopAPI.entity.key.ValoracionKey;
import com.ingesoft.grupo22.tapasTopAPI.exceptions.*;
import com.ingesoft.grupo22.tapasTopAPI.repository.DegustacionRepository;
import com.ingesoft.grupo22.tapasTopAPI.repository.LocalRepository;
import com.ingesoft.grupo22.tapasTopAPI.repository.UsuarioRepository;
import com.ingesoft.grupo22.tapasTopAPI.repository.UsuarioValoraDegustacionRepository;
import com.ingesoft.grupo22.tapasTopAPI.service.EmailService;
import com.ingesoft.grupo22.tapasTopAPI.service.UsuarioService;
import com.ingesoft.grupo22.tapasTopAPI.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static com.ingesoft.grupo22.tapasTopAPI.utils.UsuarioMapper.mapToUsuarioDto;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DegustacionRepository degustacionRepository;

    @Autowired
    private UsuarioValoraDegustacionRepository usuarioValoraDegustacionRepository;

    @Autowired
    private LocalRepository localRepository;

    @Value("${frontEndHost}")
    private String host;

    @Override
    public void register(RegisterDTO_IN registerDTO_in, MultipartFile image) throws UsernameExistException, EmailExistException {
        if(usuarioRepository.findByUsername(registerDTO_in.getUsername()).isPresent())
            throw new UsernameExistException("Usuario existente con username: " + registerDTO_in.getUsername());

        if(usuarioRepository.findByEmail(registerDTO_in.getEmail()).isPresent())
            throw new EmailExistException("Usuario existente con email: " + registerDTO_in.getEmail());
        String imagen = null;
        if(image != null && !image.isEmpty()) {
            String absolutePath = "C://TapasTop//images";
            try {
                byte[] bytesImg = image.getBytes();
                Path completePath = Paths.get(absolutePath + "//" + image.getOriginalFilename());
                Files.write(completePath, bytesImg);
                imagen = image.getOriginalFilename();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        String encodedPassword = bCryptPasswordEncoder.encode(registerDTO_in.getPassword());
        Usuario usuario = new Usuario(registerDTO_in.getUsername(), registerDTO_in.getEmail(),
                encodedPassword, registerDTO_in.getFirstName(), registerDTO_in.getLastName(),
                registerDTO_in.getAddress(), registerDTO_in.getDescription(), imagen,
                registerDTO_in.getGender(), registerDTO_in.getCountry(), false);
        usuarioRepository.save(usuario);
        String token = tokenUtils.createToken(usuario.getUsername(), usuario.getEmail());
        String url = host + "/registerVerification.html?token=" + token;
        emailService.sendEmail(registerDTO_in.getEmail(), buildConfirmationEmailHtml(registerDTO_in.getUsername(), url),
                "Confirma tu cuenta");
    }

    @Override
    public void confirmAccount(String token) throws TokenNotValidException {
        String username = tokenUtils.verificarToken(token);
        Usuario usuario = usuarioRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("El usuario con username: " + username + " no ha sido encontrado"));
        if(usuario.isEnabled()) throw new IllegalStateException("Tu cuenta ya ha sido verificada");
        usuario.setEnabled(true);
        usuarioRepository.save(usuario);
    }

    @Override
    public void enviarCorreoRecuperarPwd(String email) throws UserNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("No existe ningun usuario registrado con el email: " + email));
        String token = tokenUtils.createToken(usuario.getUsername(), email);
        String url = host + "/recoverPwdVerification.html?token=" + token;
        emailService.sendEmail(email, buildEmailHtmlChangePwd(usuario.getUsername(), url), "Recupera tu contraseña");
    }

    @Override
    public void changePwd(String username, String password) throws UserNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        usuario.setPassword(bCryptPasswordEncoder.encode(password));
        usuarioRepository.save(usuario);
    }

    @Override
    public UsuarioDTO_OUT getUsuario(String username) throws UserNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(
                ()-> new UserNotFoundException("Usuario no encontrado"));
        return mapToUsuarioDto(usuario);
    }

    @Override
    public void createDegustacion(String username, DegustacionDTO_IN degustacionDTO_in, MultipartFile image)
            throws UserNotFoundException, DegustacionExistException, LocalNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException("El usuario con username: "+
                username+ " no ha sido encontrado."));
        Local local = localRepository.findByNameIgnoreCase(degustacionDTO_in.getLocalName()).orElseThrow(()->
                new LocalNotFoundException("El local: " + degustacionDTO_in.getLocalName() + " no ha sido encontrado"));
        if(local.getDegustacionList().stream().anyMatch(d -> d.getPlateName().equalsIgnoreCase(degustacionDTO_in.getPlateName())))
            throw new DegustacionExistException("La degustación con nombre de plato/tapa: " + degustacionDTO_in.getPlateName() +
                    " ya existe para este local");
        String imageUrl = null;
        if(image != null && !image.isEmpty()){
            String absolutePath = "C://TapasTop//images";
            try {
                byte[] bytesImg = image.getBytes();
                Path completePath = Paths.get(absolutePath + "//" +image.getOriginalFilename());
                Files.write(completePath, bytesImg);
                imageUrl = image.getOriginalFilename();
            } catch (IOException e) {
                throw new IllegalStateException(e.getMessage());
            }
        }
        Degustacion degustacion = new Degustacion(degustacionDTO_in.getPlateName(), degustacionDTO_in.getDescription(),
                imageUrl, degustacionDTO_in.getType(),
                degustacionDTO_in.getOriginCountry(), degustacionDTO_in.getTasteQualifier(), new Date(), usuario, local);
        degustacionRepository.save(degustacion);
    }

    @Override
    public void rateDegustacion(String username, ValoracionDTO_IN valoracionDTO_in)
            throws UserNotFoundException, LocalNotFoundException, DegustacionNotFoundException, ValoracionExistException{
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(
                ()-> new UserNotFoundException("El usuario con: "+ username + " no ha sido encontrado"));
        Local local = localRepository.findByNameIgnoreCase(valoracionDTO_in.getLocalName()).orElseThrow(() ->
                new LocalNotFoundException("El local " + valoracionDTO_in.getLocalName() + " no existe"));
        Optional<Degustacion> degustacion = local.getDegustacionList().stream().filter
                (d -> d.getPlateName().equalsIgnoreCase(valoracionDTO_in.getPlateName())).findFirst();
        if(degustacion.isEmpty()) throw new DegustacionNotFoundException("La degustación con nombre de plato/tapa + " +
                valoracionDTO_in.getPlateName() + " no existe");
        boolean alreadyRated = degustacion.get().getUsuarioValoraDegustacionList().stream()
                .anyMatch(v -> v.getUsuario().getId().equals(usuario.getId()));
        if(alreadyRated) throw new ValoracionExistException("El usuario ya ha valorado previemante esta degustación");
        ValoracionKey valoracionKey = new ValoracionKey(usuario.getId(), degustacion.get().getId());
        UsuarioValoraDegustacion valoracion = new UsuarioValoraDegustacion(valoracionKey, valoracionDTO_in.getRate(), usuario, degustacion.get());
        usuarioValoraDegustacionRepository.save(valoracion);
    }

    @Override
    public void createLocal(String username, LocalDTO_IN localDTO_in) throws UserNotFoundException, LocalExistException {
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(
                "Usuario con username " + username + " no ha sido encontrado"));
        if(localRepository.findByNameIgnoreCase(localDTO_in.getName()).isPresent()) throw new
                LocalExistException("El local con nombre " + localDTO_in.getName() + " ya existe");
        Local local = new Local(localDTO_in.getName(), localDTO_in.getAddress(), localDTO_in.getLatitude(), localDTO_in.getLongitude(), new Date(), usuario);
        localRepository.save(local);
    }

    /*
    @Override
    public void editUsuario(String username, EditUsuarioDTO_IN editUsuarioDTO_in, MultipartFile image) throws UserNotFoundException, UsernameExistException, EmailExistException {
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException("El usuario con username: " + username + " no existe"));
        if(!editUsuarioDTO_in.getUsername().equals(username)){
            if(usuarioRepository.findByUsername(editUsuarioDTO_in.getUsername()).isPresent())
                throw new UsernameExistException("Usuario existente con nombre de usuario: " + editUsuarioDTO_in.getUsername());
            else usuario.setUsername(editUsuarioDTO_in.getUsername());
        }
        if(!editUsuarioDTO_in.getEmail().equals(usuario.getEmail())){
            if(usuarioRepository.findByEmail(editUsuarioDTO_in.getEmail()).isPresent())
                throw new EmailExistException("Usuario existente con email: " + editUsuarioDTO_in.getEmail());
            else usuario.setEmail(usuario.getEmail());
        }
        if(image != null && !image.isEmpty()) {
            String absolutePath = "C://TapasTop//images";
            try {
                byte[] bytesImg = image.getBytes();
                Path completePath = Paths.get(absolutePath + "//" + image.getOriginalFilename());
                Files.write(completePath, bytesImg);
                String imageUrl = image.getOriginalFilename();
                usuario.setPhotoUrl(imageUrl);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }else usuario.setPhotoUrl(editUsuarioDTO_in.getPhotoUrl());
        usuario.setFirstName(editUsuarioDTO_in.getFirstName());
        usuario.setLastName(editUsuarioDTO_in.getLastName());
        usuario.setAddress(editUsuarioDTO_in.getAddress());
        usuario.setDescription(editUsuarioDTO_in.getDescription());
        usuario.setGender(editUsuarioDTO_in.getGender());
        usuario.setCountry(editUsuarioDTO_in.getCountry());
        usuarioRepository.save(usuario);
    }
*/

    private String buildConfirmationEmailHtml(String name, String url) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your user</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> " +
                "<a href=\" "+  url + " \">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    private String buildEmailHtmlChangePwd(String name, String url) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Reset your password</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Please click on the below link to change your password: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> " +
                "<a href=\"" + url + "\">Go to website</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

}