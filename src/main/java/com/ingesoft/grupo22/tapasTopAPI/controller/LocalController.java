package com.ingesoft.grupo22.tapasTopAPI.controller;

import com.ingesoft.grupo22.tapasTopAPI.exceptions.DegustacionNotFoundException;
import com.ingesoft.grupo22.tapasTopAPI.exceptions.ExceptionHandling;
import com.ingesoft.grupo22.tapasTopAPI.exceptions.LocalNotFoundException;
import com.ingesoft.grupo22.tapasTopAPI.service.LocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api")
public class LocalController extends ExceptionHandling {

    @Autowired
    private LocalService localService;

    @GetMapping("/locales/{localName}/degustaciones/{plateName}")
    public ResponseEntity<?> getDegustacion(@PathVariable String localName, @PathVariable String plateName) throws DegustacionNotFoundException, LocalNotFoundException {
        return new ResponseEntity<>(localService.findDegustacion(localName, plateName), HttpStatus.OK);
    }
}
