package com.business.money.exception;

import com.business.money.exception.exceptions.NotFoundException;
import com.business.money.exception.exceptions.RoleNotFoundException;
import com.business.money.exception.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springdoc.api.ErrorMessage;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.stream.Collectors;

@RestControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorMessage> handleNotValidException(MethodArgumentNotValidException exception) {
        var errorMessage = exception.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(exception.getStatusCode()).body( new ErrorMessage(errorMessage));
    }

    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<ErrorMessage> handleEventTypeNotFound(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body( new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    private ResponseEntity<ErrorMessage> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    private ResponseEntity<ErrorMessage> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<ErrorMessage> handleBadCredentialsException(BadCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    private ResponseEntity<ErrorMessage> handleRoleNotFoundException(RoleNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<ErrorMessage> handleRuntimeException(RuntimeException exception) {
        System.out.println("runtime error: " + exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage(exception.getMessage()));
    }

}
