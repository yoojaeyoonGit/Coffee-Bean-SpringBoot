package jpabook.jpashop.service;

import jpabook.jpashop.domain.Cart;
import jpabook.jpashop.domain.CartItem;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.CartItemRepository;
import jpabook.jpashop.repository.CartRepository;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public void addCart(Member member, Item newItem, int amount) {
        Cart cart = cartRepository.findByUserId(member.getId());

        // 장바구니가 존재하지 않는다면
        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        Item item = itemRepository.findOne(newItem.getId());
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if (cartItem == null) {
            cartItem = CartItem.createCartItem(cart, item, amount);
            cartItemRepository.save(cartItem);
        } else {
            CartItem update = cartItem;
            update.setCart(cartItem.getCart());
            update.setItem(cartItem.getItem());
            update.addCount(amount);
            update.setCount(update.getCount());
            cartItemRepository.save(update);
        }

//          카트 상품 총 개수 증가
            cart.setCount(cart.getCount()+amount);
    }
    @Transactional
    public Cart findCartByMemberName(Long id) {
        Member member = memberRepository.findOne(id);
        Cart cart = cartRepository.findByUserId(member.getId());
        return cart;
    }

    @Transactional
    public Cart findCartByCartId(Long id) {
        Cart cart = cartRepository.find(id);
        return cart;
    }

    @Transactional
    public void allCartItemDelete() {
       List<CartItem> items =  cartRepository.findAll();
       for (CartItem item: items) {
           cartItemRepository.deleteById(item.getId());
       }
    }

    @Transactional
    public int cartItemPrice (Cart cart) {
        int forOneItemCost = 0;
        int everyCartItemsCost = 0;
        List <CartItem> cartItems = cart.getCart_items();
        for ( CartItem items : cartItems ) {
            forOneItemCost = items.getCount() * items.getItem().getPrice(); // 장바구니에 담긴 상품 개수 * 원상품 가격 = 총가
            everyCartItemsCost += forOneItemCost;
        }
        return everyCartItemsCost;
    }
}
