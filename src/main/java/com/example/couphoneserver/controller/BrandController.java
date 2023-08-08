package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.exception.BadRequestException;
import com.example.couphoneserver.common.exception.InternalServerErrorException;
import com.example.couphoneserver.common.response.BaseResponse;
import com.example.couphoneserver.dto.brand.GetBrandDetailResponse;
import com.example.couphoneserver.dto.brand.GetBrandResponse;
import com.example.couphoneserver.dto.brand.PostBrandRequest;
import com.example.couphoneserver.dto.brand.PostBrandResponse;
import com.example.couphoneserver.service.BrandService;
import com.example.couphoneserver.service.MemberService;
import com.example.couphoneserver.utils.S3Uploader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.BAD_REQUEST;
import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.FILE_UPLOAD_FAILED;

@Tag(name = "brands", description = "브랜드 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/brands")
public class BrandController {
    private final BrandService brandService;
    private final MemberService memberService;
    private final S3Uploader s3Uploader;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "브랜드 등록",
            description = """
                    - [ROLE_ADMIN ONLY]
                    - Header에 Access Token,
                    - Request Body에 request: 브랜드 이름(String), 보상 설명(String), 카테고리 id(Long) / file: 이미지(MultipartFile) 담아서 보내주세요!
                    """,
            security = @SecurityRequirement(name = "bearerAuth"))
    public BaseResponse<PostBrandResponse> postBrand(@RequestPart PostBrandRequest request, @RequestPart("file") MultipartFile multipartFile) {

        String brandImageUrl = s3Uploader.upload(multipartFile);

        if (brandImageUrl == null) {
            throw new InternalServerErrorException(FILE_UPLOAD_FAILED);
        }

        return new BaseResponse<>(brandService.saveBrand(request, brandImageUrl));
    }

    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    @GetMapping("")
    @Operation(summary = "브랜드 조회 (검색어 or 카테고리별)",
            description =
                    """
                            Query String으로 카테고리 ID를 담아서 보내주시면 카테고리별로 브랜드를 조회하고, 검색어 담아서 보내주시면 검색한 이름에 따라 브랜드를 조회합니다.
                            Header에 Access Token, Query String으로 정렬 옵션 담아주세요. 
                            - [ROLE_MEMBER OR ROLE_ADMIN]
                            - 1(default): 쿠폰 많은 순, 생성 시간 빠른 순
                            - 2: 생성 시간 빠른 순, 쿠폰 많은 순
                            - 3: 브랜드 이름 순
                            """,
            security = @SecurityRequirement(name = "bearerAuth"))
    public BaseResponse<List<GetBrandResponse>> getBrand(@RequestParam(required = false, value = "category-id") Long categoryId,
                                                         @RequestParam(required = false, value = "name") String name,
                                                         @RequestParam(required = false, defaultValue = "1", value = "sorted-by") int sortedBy,
                                                         Principal principal) {

        Long memberId = memberService.findMemberIdByPrincipal(principal);

        if ((categoryId != null) && (name == null)) {
            return new BaseResponse<>(brandService.findByCategoryId(memberId, categoryId, sortedBy));
        }

        if ((categoryId == null) && (name != null)) {
            return new BaseResponse<>(brandService.findByNameContaining(memberId, name, sortedBy));
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
        Long memberId = memberService.findMemberIdByPrincipal(principal);
        return new BaseResponse<>(brandService.getBrandDetail(brandId, memberId));
    }
}
