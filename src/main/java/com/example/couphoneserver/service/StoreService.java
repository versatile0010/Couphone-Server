package com.example.couphoneserver.service;

import com.example.couphoneserver.common.datatype.Coordinate;
import com.example.couphoneserver.common.exception.BrandException;
import com.example.couphoneserver.common.exception.StoreException;
import com.example.couphoneserver.domain.entity.Brand;
import com.example.couphoneserver.domain.entity.Store;
import com.example.couphoneserver.dto.store.PostNearbyStoreRequest;
import com.example.couphoneserver.dto.store.PostNearbyStoreResponse;
import com.example.couphoneserver.dto.store.PostStoreRequest;
import com.example.couphoneserver.dto.store.PostStoreResponse;
import com.example.couphoneserver.repository.BrandRepository;
import com.example.couphoneserver.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

    /*
    가게 조회
     */
    public List<PostNearbyStoreResponse> findNearbyStores(PostNearbyStoreRequest request){
        List<PostNearbyStoreResponse> storeList = getCandidateStoreList(request);
        return null;
    }

    private List<PostNearbyStoreResponse> getCandidateStoreList(PostNearbyStoreRequest request) {
        request.setDistance();
        double x = request.getLongitude();
        double y = request.getLatitude();
        double radius = request.getDistance();
        double minLongitude = x - radius;
        double maxLongitude = x + radius;
        double minLatitude = y - radius;
        double maxLatitude = y + radius;
        List<PostNearbyStoreResponse> StoreList = new ArrayList<>();
        storeRepository.findNearbyStores(minLongitude,maxLongitude,minLatitude,maxLatitude).stream().forEach(c -> {
            Coordinate coordinate = c.translateCoordinate();
            calculateDistance(x, y, coordinate);
            StoreList.add(c.translateResponse());
        });
        return StoreList;
    }

    private void calculateDistance(double x, double y, Coordinate coordinate) {
        double distanceX = Math.abs(coordinate.getLongitude() - x);
        double distanceY = Math.abs(coordinate.getLatitude() - y);
        Double distance = Math.sqrt(distanceX*distanceX+distanceY*distanceY);
        log.info(distance.toString());
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

}
