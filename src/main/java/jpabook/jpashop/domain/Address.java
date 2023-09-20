package jpabook.jpashop.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable // JPA 내장 타입이기 때문에 어딘가에 연결이 될 수 도 있다.
@Getter // 값 타입은 변경이 되면 안됨. 좋은 설계는 생성할 때만 세팅이 되고 Setter는 제공하지 않게 해야함
public class Address {
    private String city;
    private String street;
    private String zipcode;

    protected Address() {

    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }



}
