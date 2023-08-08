package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.datatype.Coordinate;
import com.example.couphoneserver.common.exception.StoreException;
import com.example.couphoneserver.common.response.BaseResponse;
import com.example.couphoneserver.dto.store.LocationInfo;
import com.example.couphoneserver.dto.store.GetNearbyStoreResponse;
import com.example.couphoneserver.dto.store.PostStoreRequest;
import com.example.couphoneserver.dto.store.PostStoreResponse;
import com.example.couphoneserver.service.StoreService;
import com.example.couphoneserver.utils.CoordinateConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    @Operation(summary = "가게 등록", description = """
            Request Body에 브랜드 아이디, 매장명, 위도, 경도, 주소를 담아서 보내주세요!
            - [ROLE_ADMIN ONLY]
            """, security = @SecurityRequirement(name = "bearerAuth"))
    public BaseResponse<PostStoreResponse> postBrand(@Validated @RequestBody PostStoreRequest request,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new StoreException(INVALID_STORE_VALUE, getErrorMessages(bindingResult));
        }
        return new BaseResponse<>(storeService.save(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/coordinate")
    @Operation(summary = "좌표 변환", description = """
            query string에 주소를 담아 보내면 좌표를 반환합니다.
            - [ROLE_ADMIN ONLY]
            """, security = @SecurityRequirement(name = "bearerAuth"))
    public BaseResponse<Coordinate> translateCoordinate(@Parameter(name = "query", description = "도로명주소", example = "서울특별시 광진구 능동로 111", in = ParameterIn.QUERY)
                                                        @RequestParam(required = true) String query) {
        return new BaseResponse<>(coordinateConverter.getCoordinate(query));
    }

    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    @GetMapping("/nearby")
    @Operation(summary = "좌표 중심 가게 반환", description = "query string에 위도, 경도, 버튼 여부를 보내면 주변 가게 리스트를 반환합니다. 좌표게: epsg:5181",
            security = @SecurityRequirement(name = "bearerAuth"))
    public BaseResponse<List<GetNearbyStoreResponse>> translateCoordinate(
            @Parameter(name = "longitude", description = "경도", example = "207005.189144674", in = ParameterIn.QUERY) @RequestParam Double longitude,
            @Parameter(name = "latitude", description = "위도", example = "449492.810069438", in = ParameterIn.QUERY) @RequestParam Double latitude,
            @Parameter(name = "is1km", description = "버튼 누른 경우 true", in = ParameterIn.QUERY) @RequestParam Boolean is1km,
            Principal principal) {
        return new BaseResponse<>(storeService.findNearbyStores(principal,
                LocationInfo.builder()
                        .longitude(longitude)
                        .latitude(latitude)
                        .is1km(is1km)
                        .build()));
    }

}
