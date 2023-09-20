package jpabook.jpashop.controller;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CouponForm {
    private String name;

    private int DiscountPrice;

    private int DiscountPercent;
}
