package jpabook.jpashop.service;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {
    private Long memberId;

    private Long orderId;

    private int count;
}
