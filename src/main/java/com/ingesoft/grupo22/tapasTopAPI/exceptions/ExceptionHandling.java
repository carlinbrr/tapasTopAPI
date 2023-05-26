package com.ingesoft.grupo22.tapasTopAPI.exceptions;

import antlr.Token;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ExceptionHandling implements ErrorController {

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> accountDisabledException(){
        return createHttpResponse(HttpStatus.UNAUTHORIZED, "Tu cuenta no está verificada");
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException(){
        return createHttpResponse(HttpStatus.UNAUTHORIZED, "Credenciales erroneas, inténtelo de nuevo");
    }
    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<HttpResponse> emailExistException(EmailExistException exception){
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
    @ExceptionHandler(UsernameExistException.class)
    public ResponseEntity<HttpResponse> usernameExistException(UsernameExistException exception){
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpResponse> notValidArguments(MethodArgumentNotValidException exception){
        return createHttpResponse(HttpStatus.BAD_REQUEST, "Confirme que todos los campos son adecuados");
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<HttpResponse> illegalStateException(IllegalStateException exception){
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException exception){
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
    @ExceptionHandler(DegustacionNotFoundException.class)
    public ResponseEntity<HttpResponse> plateNameNotFoundException(DegustacionNotFoundException exception){
        return createHttpResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }
    @ExceptionHandler(DegustacionExistException.class)
    public ResponseEntity<HttpResponse> plateExistsException(DegustacionExistException exception){
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ValoracionExistException.class)
    public ResponseEntity<HttpResponse> valoracionExistException(ValoracionExistException exception){
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(TokenNotValidException.class)
    public ResponseEntity<HttpResponse> tokenNotValidException(TokenNotValidException exception){
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(LocalExistException.class)
    public ResponseEntity<HttpResponse> localExistException(LocalExistException exception){
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(LocalNotFoundException.class)
    public ResponseEntity<HttpResponse> localNotFoundException(LocalNotFoundException exception){
        return createHttpResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message){
        HttpResponse httpResponse = new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase(), message);
        return new ResponseEntity<>(httpResponse, httpStatus);
    }
}
