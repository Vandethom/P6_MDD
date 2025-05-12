package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.security.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Map<String, Object>> handleAuthException(AuthException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("status",    HttpStatus.BAD_REQUEST.value());
        body.put("message",   e.getMessage());
        body.put("timestamp", Instant.now().toEpochMilli());
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        Map<String, Object> body = new HashMap<>();
        body.put("status",    HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("message",   "Une erreur inattendue s'est produite");
        body.put("error",     e.getMessage());
        body.put("timestamp", Instant.now().toEpochMilli());

        e.printStackTrace(); // Pour afficher la trace complète de l'erreur dans les logs
        
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
