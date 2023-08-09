package com.example.couphoneserver.repository;

import com.example.couphoneserver.domain.entity.Store;
import com.example.couphoneserver.repository.mappingInterface.StoreInfoMapping;
import com.example.couphoneserver.repository.mappingInterface.StoreMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByName(String name);

    @Query(value = "select " +
        "s.store_id,s.name as storeName,s.longitude,s.latitude, s.address, " +
            "b.brand_id, b.name as brandName, b.reward_description, b.brand_image_url, b.created_date, c.stamp_count " +
        "from store s, brand b, coupon_item c " +
        "where s.brand_id = b.brand_id and s.brand_id = c.brand_id " +
        "and c.member_id = ?1 and c.status not like 'EXPIRED' " +
        "and s.longitude between ?2 and ?3 " +
        "and s.latitude between ?4 and ?5 " +
        "group by b.brand_id " +
        "order by c.stamp_count desc " +
        "limit 10", nativeQuery = true)
    List<StoreInfoMapping> findNearbyStores(long memberId, double minX, double maxX, double minY, double maxY);

    @Query(value = "select " +
            "s.store_id,s.name,s.brand_id,s.longitude,s.latitude, s.address, b.brand_id, b.name as brandName, b.reward_description, b.brand_image_url, b.created_date  " +
            "from store s, brand b " +
            "where s.brand_id = b.brand_id and  s.longitude between ?1 and ?2 " +
            "and s.latitude between ?3 and ?4", nativeQuery = true)
    List<StoreMapping> findNearbyAdditional(double minX, double maxX, double minY, double maxY);

}
