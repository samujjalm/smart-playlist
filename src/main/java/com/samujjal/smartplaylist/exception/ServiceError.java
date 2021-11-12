package com.samujjal.smartplaylist.exception;

import lombok.Data;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;

@Data
public class ServiceError {
    @NotNull
    private final String errorMessage;
    private final String errorCode;
    private final Object details;

    public static ServiceError from(PlaylistServiceException cartServiceException) {
        return new ServiceError(cartServiceException.getMessage(), cartServiceException.getErrorCode().name(), cartServiceException.getDetails());
    }

    public static ServiceError from(MethodArgumentNotValidException ex) {
        return new ServiceError("Validation failed", "VALIDATION_ERROR", ex.getBindingResult().getAllErrors());
    }

    public static ServiceError from(ConstraintViolationException ex) {
        return new ServiceError("Validation failed", "VALIDATION_ERROR",  ex.getMessage());
    }
}
