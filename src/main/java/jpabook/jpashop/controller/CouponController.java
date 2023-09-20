package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Coupon;
import jpabook.jpashop.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @GetMapping("/coupon/register")
    public String couponRegisterPage(Model model) {
        model.addAttribute("CouponForm", new CouponForm());
        return "coupon/couponRegisterForm";
    }

    @PostMapping("/coupon/register")
    public String couponSave(@Valid CouponForm form) {
        Coupon coupon = new Coupon();
        coupon.setName(form.getName());
        coupon.setDiscountPercent(form.getDiscountPercent());
        coupon.setDiscountPrice(form.getDiscountPrice());

        couponService.couponRegister(coupon);
        return "redirect:/coupon/list";
    }

    @GetMapping("/coupon/list")
    public String coupon(Model model) {
        List<Coupon> coupons = couponService.findAllCoupons();
        model.addAttribute("coupons", coupons);

        return "coupon/couponList";
    }

//    @GetMapping("/coupon/{id}/list")
//    public String coupon(@PathVariable("id") Long id,  Model model) {
//
//
//        List<Coupon> coupons = couponService.findCoupons();
//        model.addAttribute("coupons", coupons);
//
//        return "coupon/couponList";
//    }
}