package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.response.BaseErrorResponse;
import com.example.couphoneserver.common.response.BaseResponse;
import com.example.couphoneserver.dto.category.GetCategoryResponse;
import com.example.couphoneserver.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "categories", description = "카테고리 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    @GetMapping("")
    @Operation(summary = "카테고리 정보 제공", description = """
            카테고리 정보를 제공합니다.
            - [ROLE_MEMBER OR ROLE_ADMIN]
            """, responses = {
            @ApiResponse(responseCode = "200", description = "쿼리가 null인 경우, 모든 카테고리 조회 성공", content = @Content(schema = @Schema(implementation = GetCategoryResponse.class))),
            @ApiResponse(responseCode = "200", description = "쿼리에 이름을 넣은 경우, 카테고리 조회 성공", content = @Content(schema = @Schema(implementation = GetCategoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않은 리소스 접근", content = @Content(schema = @Schema(implementation = BaseErrorResponse.class)))
    }, security = @SecurityRequirement(name = "bearerAuth"))

    public BaseResponse<List<GetCategoryResponse>> getCategory(@Parameter(name = "query", description = "카테고리의 이름(선택)", in = ParameterIn.QUERY)
                                                               @RequestParam(required = false) String query) {
        return new BaseResponse<>(categoryService.findCategory(query));
    }
}
