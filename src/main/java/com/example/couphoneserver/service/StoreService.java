package com.example.couphoneserver.service;

import com.example.couphoneserver.dto.store.PostStoreRequest;
import com.example.couphoneserver.dto.store.PostStoreResponse;
import com.example.couphoneserver.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    /*
    가게 등록
     */
    @Transactional
    public PostStoreResponse save(PostStoreRequest postStoreRequest) {
        return null;
    }

    /*
    가게 조회
     */
}
