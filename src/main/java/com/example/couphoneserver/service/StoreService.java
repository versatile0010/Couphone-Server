package com.example.couphoneserver.service;

import com.example.couphoneserver.common.exception.BrandException;
import com.example.couphoneserver.common.exception.StoreException;
import com.example.couphoneserver.domain.entity.Brand;
import com.example.couphoneserver.domain.entity.Store;
import com.example.couphoneserver.dto.store.PostStoreRequest;
import com.example.couphoneserver.dto.store.PostStoreResponse;
import com.example.couphoneserver.repository.BrandRepository;
import com.example.couphoneserver.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.BRAND_NOT_FOUND;
import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.DUPLICATE_STORE_NAME;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final BrandRepository brandRepository;

    /*
    가게 등록
     */
    @Transactional
    public PostStoreResponse save(PostStoreRequest request) {
        //매장 브랜드 찾기
        Brand brandOfStore = validateBrand(request.getBid());
        //지점명 중복 확인
        validateStoreName(request);
        //매장 등록
        Store store = storeRepository.save(request.toEntity(brandOfStore));
        return new PostStoreResponse(store.getId());
    }

    private void validateStoreName(PostStoreRequest postStoreRequest) {
//        log.info("[StoreService.validateStoreName]");
        if(storeRepository.existsByName(postStoreRequest.getName()))
            throw new StoreException(DUPLICATE_STORE_NAME);
    }

    private Brand validateBrand(Long brandId) {
//        log.info("[StoreService.validateBrand]");
        Optional<Brand> brand = brandRepository.findById(brandId);
        if(brand.isEmpty()) throw new BrandException(BRAND_NOT_FOUND);
        return brand.get();
    }

    /*
    가게 조회
     */
}
