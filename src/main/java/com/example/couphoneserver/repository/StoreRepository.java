package com.example.couphoneserver.repository;

import com.example.couphoneserver.domain.entity.Store;
import com.example.couphoneserver.dto.store.PostNearbyStoreResponse;
import com.example.couphoneserver.repository.mappingInterface.StoreInfoMapping;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByName(String name);

    @Query(value = "select " +
            "s1_0.store_id,s1_0.name,s1_0.address,s1_0.brand_id,s1_0.longitude,s1_0.latitude " +
            "from store s1_0 " +
            "where s1_0.longitude between ?1 and ?2 " +
            "and s1_0.latitude between ?3 and ?4", nativeQuery = true)
    List<StoreInfoMapping> findNearbyStores(double minX, double maxX, double minY, double maxY);

}
