package jpabook.jpashop.controller;
import jpabook.jpashop.SessionConst;
import jpabook.jpashop.domain.Cart;
import jpabook.jpashop.domain.Coupon;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    private final CouponService couponService;

    private final CartService cartService;

    @GetMapping("/order")
    public String createForm(HttpServletRequest request, Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        HttpSession session = request.getSession(false);
        Member logged = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        Member loggmember = memberService.findOne(logged.getId());

        // lazyInitial transaction 아닌 상태로 멤버를 찾게되어 에러가 발생되었다.
        System.out.println("loggedMember in Order = " + loggmember.getName());

        model.addAttribute("members", members);
        model.addAttribute("items", items);
        model.addAttribute("loggedMember", loggmember);

        return "order/orderForm";
    }

    @ResponseBody
    @RequestMapping (value = {"/order", "/orderFromCart"}, method = RequestMethod.POST)
    public String order(@RequestBody String json, HttpServletRequest request, HttpServletResponse response) { //String json
        System.out.println("json" + json);
        try {
            if(request.getServletPath().equals("/order")) {
                JSONArray array = new JSONArray(json);
                System.out.println(array);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = (JSONObject)array.get(i);
                    //orderService에 넘길 멤버 Id
                    String MemberId = (String) jsonObject.get("memberId");
                    Long MemberIdToInt = Long.parseLong(MemberId);

                    //orderService에 넘길 아이템 Id
                    String itemId = (String) jsonObject.get("itemId");
                    Long itemIdToInt = Long.parseLong(itemId);

                    //orderService에 넘길 주문 수량
                    String count = (String) jsonObject.get("count");
                    int OrderCount = Integer.parseInt(count);

                    orderService.order(MemberIdToInt, itemIdToInt, OrderCount);
                }
            } else if (request.getServletPath().equals("/orderFromCart")){
                System.out.println("json" + json);
                JSONArray forCartObject = new JSONArray(json);
//                JSONObject couponId = (JSONObject) forCartObject.getJSONObject("couponId");


                for (int i = 0; i < forCartObject.length(); i++) {
                    JSONObject cartJsonObject = (JSONObject)forCartObject.get(i);

                    int CartIdForFindMemberBeforeCast = (int) cartJsonObject.get("id");
                    Long cartIdForFindMember = Long.parseLong(String.valueOf(CartIdForFindMemberBeforeCast));

                    Cart cartOwner = cartService.findCartByCartId(cartIdForFindMember);
                    Long cartOwnerId =  cartOwner.getMember().getId();

                    JSONArray cartItemArray =  cartJsonObject.getJSONArray("cart_items");
                    for (int j = 0; j < cartItemArray.length(); j++) {
                        JSONObject finalObjectForCartOrder = (JSONObject) cartItemArray.get(j);

                        int orderCount = (int) finalObjectForCartOrder.get("count");

                        JSONObject itemObject = finalObjectForCartOrder.getJSONObject("item");

                        int itemId = (int) itemObject.get("id");
                        Long itemIdForOrder = Long.parseLong(String.valueOf(itemId));

                        orderService.order(cartOwnerId, itemIdForOrder, orderCount);
                    }
                }
                cartService.allCartItemDelete();
            }
            return "redirect:/orders";
        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);

        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}