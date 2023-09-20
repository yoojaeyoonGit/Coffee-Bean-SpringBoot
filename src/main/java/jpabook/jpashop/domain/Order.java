package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;
import org.jboss.jandex.Main;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table (name = "orders") // 적지 않으면 관례로 order로 되버림
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class  Order {
    @Id @GeneratedValue
    @JsonBackReference
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY) //order를 조회할 때 멤버를 조인시켜 함께 가져온다.
    // order와 member는 ManyToOne
    @JoinColumn (name = "member_id") // 매핑을 member_id로 한다는 의미
    @JsonBackReference
    private Member member;
    // 여기 값을 세팅하면 member_id FK값이 다른 멤버로 변경된다.

    // JPQL select o From order o; -> SQL select * from order
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    @JsonManagedReference
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Delivery delivery;

    private LocalDateTime orderDate; //주문 시간
    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문 상태[ORDER, CANCEL]

    //==연관 관계 메서드 //
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }


    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //=생성 메소드=//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    /** 주문 취소 **/
    public void cancel() {
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 불가능합니다");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

    /** 전체 주문 가격 조회 **/
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems){
            totalPrice +=  orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}

