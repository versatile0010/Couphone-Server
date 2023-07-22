package com.example.couphoneserver.common.exception.jwt.bad_request;


import com.example.couphoneserver.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class JwtUnsupportedTokenException extends JwtBadRequestException {

    private final ResponseStatus exceptionStatus;

    public JwtUnsupportedTokenException(ResponseStatus exceptionStatus) {
        super(exceptionStatus);
        this.exceptionStatus = exceptionStatus;
    }

}
