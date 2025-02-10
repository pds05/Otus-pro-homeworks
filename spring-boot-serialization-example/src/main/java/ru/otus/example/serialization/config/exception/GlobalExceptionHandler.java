package ru.otus.example.serialization.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.example.serialization.dto.ErrorDto;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SmsException.class)
    public ResponseEntity<ErrorDto> catchResourceNotFound(final SmsException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<ErrorDto>(new ErrorDto(e.getCauseCode(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> catchApplicationException(final Throwable ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<ErrorDto>(new ErrorDto("INTERNAL_ERROR", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
