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

import java.util.*;

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.BRAND_NOT_FOUND;
import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.DUPLICATE_STORE_NAME;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final BrandRepository brandRepository;

    private static final int ELEMENT = 4;

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
        Collections.sort(storeList, new Comparator<PostNearbyStoreResponse>() {
            @Override
            public int compare(PostNearbyStoreResponse o1, PostNearbyStoreResponse o2) {
                return o1.getDistance() > o2.getDistance()? 1: -1;
            }
        });
        log.info(String.valueOf(storeList.size()));
        int numOfElement = storeList.size()>=ELEMENT?ELEMENT:storeList.size();
        return storeList.subList(0,numOfElement);
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
            PostNearbyStoreResponse response = c.translateResponse();
            Coordinate coordinate = c.translateCoordinate();
            response.setDistance(calculateDistance(x,y,coordinate));
            StoreList.add(response);
        });
        return StoreList;
    }

    private double calculateDistance(double x, double y, Coordinate coordinate) {
        double distanceX = Math.abs(coordinate.getLongitude() - x);
        double distanceY = Math.abs(coordinate.getLatitude() - y);
        return Math.sqrt(distanceX*distanceX+distanceY*distanceY);
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
