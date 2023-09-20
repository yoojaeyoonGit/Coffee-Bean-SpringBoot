package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Cart {

    @Id @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    private int count; // 카트에 담긴 총 상품 개수

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonManagedReference
    @OneToMany(mappedBy = "cart")
    private List<CartItem> cart_items = new ArrayList<>();


    // 연관관계 메소드
    public void setMember(Member member) {
        this.member = member;
        member.setCart(this);
    }

    public void addCartItem(CartItem cartItem){
        cart_items.add(cartItem);
        cartItem.setCart(this);
    }


    //=생성 메소드=//
    public static Cart createCart(Member member) {
        System.out.println("What the fuck");
        Cart cart = new Cart();
        cart.setCount(0);
        cart.setMember(member);
        return cart;
    }
}