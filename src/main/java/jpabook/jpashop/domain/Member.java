package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id") //id 이름 설정
    private Long Id;
    private String name;

    private String password;
    
    @Embedded // 내장타입을 포함했다라는 어노테이션으로 매핑 해주면 된다
              // Embedded 나 Embeddable 둘 중 하나만 있어도 되지만 두 개 다 쓰는 것을 권장
    private Address address;

    @JsonManagedReference
    @OneToMany(mappedBy = "member") //하나의 회원이 여러개의 상품을 주문하기 때문에, 읽기 전용
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private List<Order> orders = new ArrayList<>();
    // 여기 값을 넣는 다고 해서 FK값이 변경되지 않는다.

    @JsonManagedReference
//    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @OneToOne(mappedBy = "member")
    private Cart cart;

    @OneToMany(mappedBy = "member")
    private List<Coupon> coupons = new ArrayList<>();
}

// 관계 주인을 잡는 이유는 예를들어 order 값을 오더 class에서도 바끌 수 있고 Member class에서도 list값을 변경할 수도 있는데
// JPA는 무엇을 읽어야 하는 지 혼동이 온다. 이런 경우를 방지 하기 위해 관계 주인을 설정하여 JPA가 관계 주인을 참조하게 하는 것이다.