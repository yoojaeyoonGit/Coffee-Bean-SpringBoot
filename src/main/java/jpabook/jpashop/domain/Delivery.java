package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    @JsonBackReference
    private Order order;

    @Embedded
    private  Address address;

    @Enumerated(EnumType.STRING) //EnumType.ORDINAL 절대 사용 금지, 숫자로 세기 때문에 값이 밀리면 다른 값이 나옴
    private DeliveryStatus status; // READY, COMP
}
