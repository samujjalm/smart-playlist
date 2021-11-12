package com.samujjal.smartplaylist.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PlaylistServiceException.class)
    public ResponseEntity<ServiceError> handlePlaylistServiceException(PlaylistServiceException exception) {
        ServiceError serviceError = ServiceError.from(exception);
        switch (exception.getErrorCode()) {
            case LYRICS_NOT_FOUND:
            case NO_MATCHING_SONGS:
                return ResponseEntity.status(NOT_FOUND).body(serviceError);
            case PLAYLIST_NOT_FOUND:
                return ResponseEntity.status(BAD_REQUEST).body(serviceError);
            default:
                log.error("Internal Error with Playlist Service", exception);
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(serviceError);
        }
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(ex, ServiceError.from(ex), headers, status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServiceError> handleUnexpected(Exception exception) {
        log.error("Unexpected exception", exception);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ServiceError(exception.getMessage(), null, null));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ServiceError> handleConstraintViolationException(ConstraintViolationException exception) {
        return ResponseEntity.badRequest().body(ServiceError.from(exception));
    }
}
