package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Cart;
import jpabook.jpashop.domain.CartItem;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.item.Item;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@AllArgsConstructor
public class CartRepository {
    private final EntityManager em;
    private final MemberRepository memberRepository;

    public void save(Cart cart) {
        em.persist(cart);
    }

    public Cart find(Long id){
        return em.find(Cart.class, id);
    }

    public Cart findByUserId(Long memberId) {
        Member member = memberRepository.findOne(memberId);

        Cart cart = member.getCart();
        System.out.println("HERE IS CART ID" + member.getCart());
        if (cart == null) {
            return cart;
        }
        return em.find(Cart.class, cart.getId());
    }

    public List<CartItem> findAll() {
        return em.createQuery("select c from CartItem c", CartItem.class)
                .getResultList();
    }
}