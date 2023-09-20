package jpabook.jpashop.controller;


import jpabook.jpashop.SessionConst;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;
    @RequestMapping("/" )
    public String home(HttpServletRequest request, Model model) {
        log.info("home controller");
        HttpSession session = request.getSession(false);

        if (session == null) {
            System.out.println("시발뭐여");
            if (memberService.findMembers().size() == 0) {
                System.out.println("시발 뭐야");
                return "redirect:/members/new";
            } else {
                return "redirect:/login";
            }
        }

        Member loginmember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);


        Member member = (Member) session.getAttribute("loginMember");
        model.addAttribute("member", member);
        System.out.println(member.getId());
        return "home";
    }
}
