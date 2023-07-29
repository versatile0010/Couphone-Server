package com.example.couphoneserver.common.exception;

import com.example.couphoneserver.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class BrandException extends RuntimeException {
    private final ResponseStatus exceptionStatus;

    public BrandException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public BrandException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }
}
