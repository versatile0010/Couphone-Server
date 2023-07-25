package com.example.couphoneserver.domain.service;

import com.example.couphoneserver.common.exception.DatabaseException;
import com.example.couphoneserver.domain.dto.category.CategoryResponse;
import com.example.couphoneserver.domain.entity.ParentCategory;
import com.example.couphoneserver.repository.ParentCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.PARENT_CATEGORY_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParentCategoryService {
    private final ParentCategoryRepository parentCategoryRepository;

    @Transactional
    public List<CategoryResponse> findCategoryById(String categoryName){
        if(categoryName==null){
            List<CategoryResponse> categoryResponses = new ArrayList<CategoryResponse>();
            List<ParentCategory> parentCategories =  parentCategoryRepository.findAll();
            for(ParentCategory category: parentCategories){
                System.out.println(category.toString());
                System.out.println("GetID: "+category.getName());
                System.out.println("GetName: "+category.getId());
                categoryResponses.add(new CategoryResponse(category.getId(),category.getName()));
            }
            return categoryResponses;
        }
        List<CategoryResponse> categoryResponse = parentCategoryRepository.findByName(categoryName);
        if(categoryResponse.size() < 1) return categoryResponse;
        throw new DatabaseException(PARENT_CATEGORY_NOT_FOUND);
    }


}
