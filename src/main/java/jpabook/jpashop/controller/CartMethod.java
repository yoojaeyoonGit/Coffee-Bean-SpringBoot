package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.CartService;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

@RequiredArgsConstructor
public class CartMethod {
    private final MemberService memberService;
    private final ItemService itemService;
    private final CartService cartService;
    public void jsonFormOrder(String json) {
        JSONArray cartJsonArray = new JSONArray(json);
        for (int i = 0; i < cartJsonArray.length(); i++) {
            JSONObject jsonObjectForAddCart = (JSONObject) cartJsonArray.get(i);

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
    }
}
