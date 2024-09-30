package com.inditex.inditex.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebInputException;


/**
 * Se podria informar con mas detalle del error, pero creo que esa informacion
 * no es relevante para el usuario final y mucho menos si el servicio es publico.
 */
@Slf4j
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<Object> handleTypeMismatch(ServerWebInputException e) {
        log.error("ServerWeb Input Error", e);
        return ResponseEntity.status(e.getStatusCode()).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Validation Error", e);
        return ResponseEntity.status(e.getStatusCode()).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception e) {
        log.error("Generic Error", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }

}
