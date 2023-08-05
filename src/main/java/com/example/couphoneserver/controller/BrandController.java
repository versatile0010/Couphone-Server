package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.exception.BadRequestException;
import com.example.couphoneserver.common.response.BaseResponse;
import com.example.couphoneserver.dto.brand.GetBrandDetailResponse;
import com.example.couphoneserver.dto.brand.GetBrandResponse;
import com.example.couphoneserver.dto.brand.PostBrandRequest;
import com.example.couphoneserver.dto.brand.PostBrandResponse;
import com.example.couphoneserver.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.BAD_REQUEST;

@Tag(name = "brands", description = "브랜드 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/brands")
public class BrandController {
    private final BrandService brandService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    @Operation(summary = "브랜드 등록",
            description = """
                    Request Body에 브랜드 이름, 보상 설명, 이미지 url, 카테고리 id 담아서 보내주세요!
                    - [ROLE_ADMIN ONLY]
                    """,
            security = @SecurityRequirement(name = "bearerAuth"))
    public BaseResponse<PostBrandResponse> postBrand(@RequestBody PostBrandRequest request) {
        return new BaseResponse<>(brandService.saveBrand(request));
    }

    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    @GetMapping("")
    @Operation(summary = "브랜드 조회",
            description =
                    """
                            브랜드를 검색어 또는 카테고리별로 조회합니다.
                            - [ROLE_MEMBER OR ROLE_ADMIN]
                            - Header에 Access Token 담아주세요!
                            - Query String으로 카테고리 ID를 담아서 보내주시면 카테고리별로 브랜드를 조회하고, 검색어 담아서 보내주시면 검색한 이름에 따라 브랜드를 조회합니다.
                            """,
            security = @SecurityRequirement(name = "bearerAuth"))
    public BaseResponse<List<GetBrandResponse>> getBrand(@RequestParam(required = false, value = "category-id") Long categoryId,
                                                         @RequestParam(required = false, value = "name") String name,
                                                         Principal principal) {

        if ((categoryId != null) && (name == null)) {
            return new BaseResponse<>(brandService.findByCategoryId(principal, categoryId));
        }

        if ((categoryId == null) && (name != null)) {
            return new BaseResponse<>(brandService.findByNameContaining(principal, name));
        }

        throw new BadRequestException(BAD_REQUEST);
    }

    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    @GetMapping("/{brand-id}")
    @Operation(summary = "브랜드 상세 조회",
            description = """
                    브랜드를 상세 조회합니다.
                    - [ROLE_MEMBER OR ROLE_ADMIN]
                    - Path Vriable로 브랜드 ID, Header에 Access Token 담아서 보내주세요!
                    """,
            security = @SecurityRequirement(name = "bearerAuth"))
    public BaseResponse<GetBrandDetailResponse> getBrandDetail(@PathVariable(value = "brand-id") Long brandId,
                                                               Principal principal) {

        return new BaseResponse<>(brandService.getBrandDetail(brandId, principal));
    }
}
