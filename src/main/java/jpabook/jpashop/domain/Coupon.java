package jpabook.jpashop.domain;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Coupon {
    @Id @GeneratedValue
    private Long id;

    private String name;

    private int DiscountPrice;

    private int DiscountPercent;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
