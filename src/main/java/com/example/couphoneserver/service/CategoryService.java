package com.example.couphoneserver.service;

import com.example.couphoneserver.common.exception.CategoryException;
import com.example.couphoneserver.domain.entity.Category;
import com.example.couphoneserver.dto.category.GetCategoryResponse;
import com.example.couphoneserver.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.CATEGORY_NOT_FOUND;

@Service
@Slf4j
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<GetCategoryResponse> findCategory(String query) {
        List<GetCategoryResponse> resultCategories = new ArrayList<>();
        if(query == null) query = "%%";
        List<Category> categories = categoryRepository.findByNameLike(query);
        if(categories.size() < 1)
            throw new CategoryException(CATEGORY_NOT_FOUND);
        for (Category category:categories)
            resultCategories.add(GetCategoryResponse.builder()
                    .CategoryId(category.getId())
                    .CategoryName(category.getName()).build());
        return resultCategories;
    }


}
