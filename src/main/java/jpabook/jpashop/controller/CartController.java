package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Cart;
import jpabook.jpashop.domain.Coupon;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.CartItemRepository;
import jpabook.jpashop.repository.CartRepository;
import jpabook.jpashop.service.CartService;
import jpabook.jpashop.service.CouponService;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final MemberService memberService;

    private final CartItemRepository cartItemRepository;

    private final CouponService couponService;
    private final ItemService itemService;



    @GetMapping("/cart/{id}")
    public String userCartPage(@PathVariable("id") Long id, Model model) {
        try{
            Member loggedMember = memberService.findOne(id);
            Cart cart = cartService.findCartByMemberName(loggedMember.getId());
            int cartCost = cartService.cartItemPrice(cart);
            List<Coupon> coupons = couponService.findAllCoupons();

            model.addAttribute("loggedMember", loggedMember);
            model.addAttribute("cart", cart);
            model.addAttribute("cartCost", cartCost);
            model.addAttribute("coupons", coupons);

            return "cart/cartList";
        }
        catch (Exception e) {
            e.printStackTrace();
            return "cart/noCart";
        }
    }

    @PostMapping
    @RequestMapping(value = "/cart/{id}", method = RequestMethod.POST)
    public String cart(@RequestBody String json) { //String json
        try {
            JSONArray cartJsonArray = new JSONArray(json);
            for (int i = 0; i < cartJsonArray.length(); i++) {
                JSONObject jsonObjectForAddCart = (JSONObject)cartJsonArray.get(i);

                //addCart에 넘길 로그인된 멤버 Id
                String cartMemberId = (String) jsonObjectForAddCart.get("memberId");
                Long CartMemberIdToLong = Long.parseLong(cartMemberId);
                Member CartMember = memberService.findOne(CartMemberIdToLong);

                //addCart에 넘길 아이템 Id
                String cartItemId = (String) jsonObjectForAddCart.get("itemId");
                Long cartItemIdToLong = Long.parseLong(cartItemId);
                Item CartItem = itemService.findOne(cartItemIdToLong);

                //addCart에 넘길 주문 수량
                String cartAmount = (String) jsonObjectForAddCart.get("count");
                int cartItemAmount = Integer.parseInt(cartAmount);
                cartService.addCart(CartMember, CartItem, cartItemAmount);
            }
            return "redirect:/cart/{id}";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/order";
        }
    }
}
