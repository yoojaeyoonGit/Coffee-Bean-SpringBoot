package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository //컴포넌트 스캔에 의해_ 자동으로 스프링 빈에 등록되어 관리
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em; //spring이 EntityManager를 만들어서 주입해준다.
    public void save(Member member){
        em.persist(member); // 영속성 컨텍스트에 member 엔티티 저장
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id); // 정보 반환  첫 번째 타입, 두 번째 pk
    }



    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class) // 첫 번째 JPQL 사용, 두 번째 반환타입
                .getResultList();
        //SQL은 테이블에 대상으로 쿼리하는데 JPQL은 엔티티 객체를 대상으로 쿼리한다.
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name",name)
                .getResultList();
    }

    public Member findOneByLoginId(String memberLoginId) {
        return em.createQuery("select m from Member m where m.member_login_id= :memberLoginId", Member.class)
                .setParameter("memberLoginId", memberLoginId)
                .getSingleResult();
    }
}
