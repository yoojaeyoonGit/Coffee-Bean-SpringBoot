package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    public Member login(Long id, String password) {
        Member member = memberRepository.findOne(id);
        if(!member.getPassword().equals(password)){
            System.out.println("Wrong password");
            member = null;
        }
        return member;
    }
}
