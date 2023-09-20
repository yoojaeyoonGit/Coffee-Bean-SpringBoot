package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) //junit 실행할 때 스프링과 함께 실행하고자 할 때
@SpringBootTest //스프링으 띄운 상태로 테스트 하기 위해
@Transactional // spring transactional
public class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test //test란 무엇이 주어졌고 이렇게 하면, 이렇게 된다, 라는 스타일이 테스트
    @Rollback(false)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");
        //when
        Long saveId = memberService.join(member);

        //then
        //em.flush(); // db에 무조건 쿼리를 날리는 것
        Assert.assertEquals(member, memberRepository.findOne(saveId));
        // jpa에서 같은 트렌젝션 안에서 같은 엔티티 id(pk)값이 똑같으면 같은 영속성 컨텍스트에서 똑같은 걸로 관리
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

       //when
        memberService.join(member1);
        memberService.join(member2);
        //then
        fail("예외가 발생해야 한다.");
    } 
}