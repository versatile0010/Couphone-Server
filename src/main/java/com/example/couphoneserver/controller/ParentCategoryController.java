package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.annotation.NoAuth;
import com.example.couphoneserver.common.response.BaseResponse;
import com.example.couphoneserver.domain.dto.category.CategoryResponse;
import com.example.couphoneserver.domain.service.ParentCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/category")
@RequiredArgsConstructor
public class ParentCategoryController {

    private final ParentCategoryService parentCategoryService;

    @NoAuth
    @GetMapping("")
    public BaseResponse<List<CategoryResponse>> getCategoryInfo(@RequestParam(required = false) String name) {
        log.error("[CategoryController.getCategoryInfo]");

        return new BaseResponse(parentCategoryService.findCategoryById(name));
    }
}
