package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.datatype.Coordinate;
import com.example.couphoneserver.common.exception.StoreException;
import com.example.couphoneserver.common.response.BaseResponse;
import com.example.couphoneserver.dto.store.PostCoordinateRequest;
import com.example.couphoneserver.dto.store.PostStoreRequest;
import com.example.couphoneserver.dto.store.PostStoreResponse;
import com.example.couphoneserver.service.StoreService;
import com.example.couphoneserver.utils.CoordinateConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.INVALID_STORE_VALUE;
import static com.example.couphoneserver.utils.BindingResultUtils.getErrorMessages;

@Tag(name = "store", description = "가게 관련 API 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {
    private final StoreService storeService;
    private final CoordinateConverter coordinateConverter;

    @PostMapping("")
    @Operation(summary = "가게 등록", description = "Request Body에 브랜드 아이디, 매장명, 위도, 경도, 주소를 담아서 보내주세요!")
    public BaseResponse<PostStoreResponse> postBrand(@Validated @RequestBody PostStoreRequest request,
                                                     BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new StoreException(INVALID_STORE_VALUE,getErrorMessages(bindingResult));
        }
        return new BaseResponse<>(storeService.save(request));
    }

    @PostMapping("/coordinate")
    @Operation(summary = "좌표 변환", description = "Request Body에 주소를 담아 보내면 좌표를 반환합니다.")
    public BaseResponse<Coordinate> translateCoordinate(@Validated @RequestBody PostCoordinateRequest request,
                                              BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new StoreException(INVALID_STORE_VALUE,getErrorMessages(bindingResult));
        }
        return new BaseResponse<>(coordinateConverter.getCoordinate(request.getAddress()));
    }
}
