package com.example.couphoneserver.service;

import com.example.couphoneserver.domain.entity.CouponItem;
import com.example.couphoneserver.repository.CouponItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.couphoneserver.domain.CouponItemStatus.EXPIRED;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SchedulerService {
    private final CouponItemRepository couponItemRepository;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정마다 1회 실행
    @Transactional(readOnly = false) // write DB
    public void couponScheduler() {
        log.info("[쿠폰 스케쥴러가 실행됩니다. 현재 시간 : " + LocalDateTime.now());
        log.info("[만료 기간이 지난 쿠폰들은 모두 만료 상태로 일괄 변경합니다.]");
        List<CouponItem> coupons = couponItemRepository.findAll(); // 쿠폰을 모두 가져와서
        for (CouponItem couponItem : coupons) {
            if (couponItem.isExpired() && couponItem.getStatus() != EXPIRED) { // 만료되어야 하면
                String couponId = String.valueOf(couponItem.getId());
                log.info("[ coupon id: " + couponId + " 가 만료되어, Status 를 EXPIRED 으로 변경하였습니다.");
                couponItem.setStatus(EXPIRED); // 만료 상태로 변경함
            }
        }
    }
}
