package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Cart;
import jpabook.jpashop.domain.CartItem;
import jpabook.jpashop.domain.Member;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@AllArgsConstructor
public class CartItemRepository {
    private final EntityManager em;
    private final CartRepository cartRepository;
    public CartItem findByCartIdAndItemId(Long cartId, Long itemId) {
        Cart cart = cartRepository.find(cartId);
        CartItem cartItem = null;
        for (int i = 0; i < cart.getCart_items().size(); i++) {
            CartItem item =  cart.getCart_items().get(i);
            if (item.getItem().getId() == itemId) {
                cartItem = item;
            }
        }
        return cartItem;
    }
    @Transactional
    public void deleteById(Long cartItemId) {
        CartItem item = em.find(CartItem.class, cartItemId);
        em.remove(item);
    }

    public void save(CartItem cartItem){
        em.persist(cartItem); // 영속성 컨텍스트에 member 엔티티 저장
    }
}