package com.example.couphoneserver.common.exception;

import com.example.couphoneserver.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class MemberException extends RuntimeException{
    private final ResponseStatus responseStatus;
    public MemberException(ResponseStatus responseStatus){
        super(responseStatus.getMessage());
        this.responseStatus = responseStatus;
    }
}
