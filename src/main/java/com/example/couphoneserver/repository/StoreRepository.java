package com.example.couphoneserver.repository;

import com.example.couphoneserver.domain.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    //TODO 1. 가게 등록
    //TODO 2. 최단 거리 가게 조회
}
