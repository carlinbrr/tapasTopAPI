package com.ingesoft.grupo22.tapasTopAPI.utils;

import com.ingesoft.grupo22.tapasTopAPI.exceptions.TokenNotValidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtils {
    private final static String ACCESS_TOKEN_SECRET = "ositodepeluchedsa6f567ds7adasd67a795ds9adas7a98";
    private final static Long ACCESS_TOKEN_VALIDITY_SECONDS = 1000L;

    @Autowired
    private UserDetailsService userDetailsService;

    /*Se envia el nombre porque se pueden enviar datos con el token
     * Este token es un simple String*/

    public String createToken(String username, String email) {
        long expirationTime = ACCESS_TOKEN_VALIDITY_SECONDS * 1000;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        //Se crea mapa donde meteremos datos adicionales para que se envien con el token
        Map<String, Object> extraData = new HashMap<>();
        extraData.put("email", email);

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expirationDate)
                .addClaims(extraData)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .compact();
    }

    /*El token  que es acepetado por spring es de tipo usernamepasswordauthenticationtoken
     * para el proceso de autorizacion (Acceder a endopoint mediante el token)
     * Pasamos el token de string al tipo adecuado verificando si es valido*/
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            return new UsernamePasswordAuthenticationToken(verificarToken(token), null, Collections.EMPTY_LIST);
        } catch (TokenNotValidException e) {
            return null;
        }
    }

    public String verificarToken(String token) throws TokenNotValidException {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String username = claims.getSubject();
            userDetailsService.loadUserByUsername(username);
            return username;
        }catch(Exception e){
           throw new TokenNotValidException("El token no es válido");
        }
    }

}
