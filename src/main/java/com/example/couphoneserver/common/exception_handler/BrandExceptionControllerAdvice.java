package com.example.couphoneserver.common.exception_handler;

import com.example.couphoneserver.common.exception.BrandException;
import com.example.couphoneserver.common.exception.CategoryException;
import com.example.couphoneserver.common.response.BaseErrorResponse;
import jakarta.annotation.Priority;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.INVALID_BRAND_VALUE;

@Priority(0)
@RestControllerAdvice
public class BrandExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BrandException.class)
    public BaseErrorResponse handle_BrandException(CategoryException e) {
        return new BaseErrorResponse(INVALID_BRAND_VALUE, e.getMessage());
    }

}