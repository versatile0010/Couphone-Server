package com.example.couphoneserver.repository;

import com.example.couphoneserver.domain.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
