package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponRepository {
    final EntityManager em;
    public void save(Coupon coupon){
        em.persist(coupon);
    }

    public Coupon find(Long couponId){
        return em.find(Coupon.class, couponId);
    }

    public List<Coupon> findAll() {
        return em.createQuery("select c from Coupon c", Coupon.class)
                .getResultList();
    }


//    public void remove()
}
