package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;
    //    private final me em;

    public void save(Item item){ // 아이템은 아이디에 저장하기 전까지 Id를 가지지 않음
        if(item.getId() == null){ // 즉, 이 if문은 완전히 새로운 객체를 말하는 것 찾고 persist 하는 것
            em.persist(item);
        } else {
            em.merge(item); // 강제 업데이트
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
