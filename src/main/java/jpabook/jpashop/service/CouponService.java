package jpabook.jpashop.service;

import jpabook.jpashop.domain.Coupon;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {
    private final CouponRepository couponRepository;

    @Transactional
    public void couponRegister(Coupon coupon){
        couponRepository.save(coupon);
    }

    public Coupon findOne(Long couponId){
        return couponRepository.find(couponId);
    }

    public List<Coupon> findAllCoupons() {
        return couponRepository.findAll();
    }
}
