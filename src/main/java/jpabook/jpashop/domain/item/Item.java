package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.CartItem;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속 관계 전략을 지정, 전략은 부모 클래스에서 잡아주어야 함
                                                      // 사용할 전략은 SINGLE_TABLE 전략
@DiscriminatorColumn(name = "dtype") // 구분을 기준으로 선택, 다른 클래스에 DiscriminatorValue("값") 으로 선택하게 도와주는 듯함
@Getter @Setter
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

//    @OneToMany(mappedBy = "item")
//    private CartItem cartItem;


    // 비즈니스 로직

    /**재고 증가**/
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /**재고 감소**/
    public void removeStock(int quantity){
        int restStock = this.stockQuantity -= quantity;
        if(restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}




