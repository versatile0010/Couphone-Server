package com.example.couphoneserver.common.response.status;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum BaseExceptionResponseStatus implements ResponseStatus {

    /**
     * 1000: 요청 성공 (OK)
     */
    SUCCESS(1000, HttpStatus.OK.value(), "요청에 성공하였습니다."),

    /**
     * 2000: Request 오류 (BAD_REQUEST)
     */
    BAD_REQUEST(2000, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 요청입니다."),
    URL_NOT_FOUND(2001, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 URL 입니다."),
    METHOD_NOT_ALLOWED(2002, HttpStatus.METHOD_NOT_ALLOWED.value(), "해당 URL에서는 지원하지 않는 HTTP Method 입니다."),

    /**
     * 3000: Server, Database, AWS 오류 (INTERNAL_SERVER_ERROR)
     */
    SERVER_ERROR(3000, HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버에서 오류가 발생하였습니다."),
    DATABASE_ERROR(3001, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스에서 오류가 발생하였습니다."),
    BAD_SQL_GRAMMAR(3002, HttpStatus.INTERNAL_SERVER_ERROR.value(), "SQL에 오류가 있습니다."),
    FILE_UPLOAD_FAILED(3003, HttpStatus.INTERNAL_SERVER_ERROR.value(), "파일 업로드에 실패했습니다."),
    IMAGE_RESIZE_ERROR(3004,HttpStatus.INTERNAL_SERVER_ERROR.value(),"파일 크기 조정에 실패했습니다."),
    INVALID_FILE_FORMAT(3005, HttpStatus.INTERNAL_SERVER_ERROR.value(),"파일 형식이 이미지가 아닙니다."),

    /**
     * 4000: Authorization 오류
     */
    JWT_ERROR(4000, HttpStatus.UNAUTHORIZED.value(), "JWT에서 오류가 발생하였습니다."),
    TOKEN_NOT_FOUND(4001, HttpStatus.BAD_REQUEST.value(), "토큰이 HTTP Header에 없습니다."),
    UNSUPPORTED_TOKEN_TYPE(4002, HttpStatus.BAD_REQUEST.value(), "지원되지 않는 토큰 형식입니다."),
    INVALID_TOKEN(4003, HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 토큰입니다."),
    MALFORMED_TOKEN(4004, HttpStatus.UNAUTHORIZED.value(), "토큰이 올바르게 구성되지 않았습니다."),
    EXPIRED_TOKEN(4005, HttpStatus.UNAUTHORIZED.value(), "만료된 토큰입니다."),
    TOKEN_MISMATCH(4006, HttpStatus.UNAUTHORIZED.value(), "로그인 정보가 토큰 정보와 일치하지 않습니다."),

    /**
     *  5000: Member 오류
     */
    DUPLICATED_MEMBER_EXCEPTION(5000, HttpStatus.BAD_REQUEST.value(), "중복된 회원 값 입니다."),
    MEMBER_NOT_FOUND(5001, HttpStatus.BAD_REQUEST.value(), "회원을 찾을 수 없습니다."),
    MEMBER_PIN_NOT_MATCHED(5002, HttpStatus.UNAUTHORIZED.value(), "핀 번호 검증에 실패하였습니다."),

    /**
     * 6000: 카테고리 오류
     */
    CATEGORY_NOT_FOUND(6000, HttpStatus.BAD_REQUEST.value(), "카테고리가 존재하지 않습니다."),

    /**
     * 7000: 브랜드 오류
     */
    BRAND_NOT_FOUND(7000, HttpStatus.BAD_REQUEST.value(), "브랜드가 존재하지 않습니다."),
    DUPLICATE_BRAND_NAME(7001, HttpStatus.BAD_REQUEST.value(), "중복된 브랜드 이름이 존재합니다."),
    /**
     * 8000: 가게 오류
     */
    INVALID_STORE_VALUE(8000,HttpStatus.BAD_REQUEST.value(), "가게 등록에 유효하지 않은 정보입니다."),
    COORDINATE_NOT_FOUND(8001,HttpStatus.BAD_REQUEST.value(), "좌표를 찾을 수 없습니다."),
    ADDRESS_NOT_FOUND(8001,HttpStatus.BAD_REQUEST.value(), "도로명주소를 찾을 수 없습니다."),
    DUPLICATE_STORE_NAME(8002,HttpStatus.BAD_REQUEST.value(), "중복된 지점이 존재합니다."),
    NEARBY_STORE_NOT_FOUND(8003,HttpStatus.BAD_REQUEST.value(), "좌표 기준 주변 가게가 없습니다."),

    /**
     * 9000: 쿠폰 오류
     */
    COUPON_NOT_FOUND(9000, HttpStatus.BAD_REQUEST.value(), "쿠폰이 존재하지 않습니다."),
    COUPON_NOT_COLLECT(9001, HttpStatus.BAD_REQUEST.value(), "적립할 수 없는 쿠폰입니다."),
    COUPON_NOT_ACTIVE(9002, HttpStatus.BAD_REQUEST.value(), "사용할 수 없는 쿠폰입니다."),
    DUPLICATE_COUPON_INACTIVE(9003, HttpStatus.BAD_REQUEST.value(), "적립 가능한 쿠폰이 이미 존재합니다.");

    private final int code;
    private final int status;
    private final String message;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
