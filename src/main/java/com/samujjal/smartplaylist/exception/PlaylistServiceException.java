package com.samujjal.smartplaylist.exception;

import lombok.Getter;

public class PlaylistServiceException extends RuntimeException {

    private static final long serialVersionUID = -2981371376570946836L;
    @Getter
    private final ErrorCode errorCode;

    @Getter
    private final transient Object details;

    public PlaylistServiceException(ErrorCode errorCode, Object details, String message) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }
}
