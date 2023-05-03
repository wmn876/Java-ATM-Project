package com.uni.atm.advice;

import com.uni.atm.exception.InvalidRequestException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.uni.atm.exception.AuthFailedException;
import com.uni.atm.exception.ResourceNotExistException;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class BusinessExceptionHandler {

    @ExceptionHandler(AuthFailedException.class)
    public ResponseEntity<Error> handleAuthFailedException(AuthFailedException ex) {
        log.error("Failed", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(ex.getLocalizedMessage()));

    }

    @ExceptionHandler(ResourceNotExistException.class)
    public ResponseEntity<Error> handleResourceNotFoundException(ResourceNotExistException ex) {
        log.error("Failed", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(ex.getLocalizedMessage()));

    }


    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Error> handleInvalidRequestException(InvalidRequestException ex) {
        log.error("Failed", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(ex.getLocalizedMessage()));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Failed", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(ex.getBindingResult().getAllErrors().stream().map(
                DefaultMessageSourceResolvable::getDefaultMessage
        ).collect(Collectors.joining(", "))));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleServerExceptions(Exception ex) {
        log.error("Failed", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error(ex.getLocalizedMessage()));

    }

}
