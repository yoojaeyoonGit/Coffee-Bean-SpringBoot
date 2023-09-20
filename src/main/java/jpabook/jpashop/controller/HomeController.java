package jpabook.jpashop.controller;


import jpabook.jpashop.SessionConst;
import jpabook.jpashop.domain.Member;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class HomeController {
    @RequestMapping("/" )
    public String home(HttpServletRequest request, Model model) {
        log.info("home controller");
        HttpSession session = request.getSession(false);
        if(session == null) {
            return "redirect:/members/new";
        }

        Member loginmember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        if (loginmember == null) {
            return "redirect:/members/new";
        }

        Member member = (Member) session.getAttribute("loginMember");
        model.addAttribute("member", member);
        System.out.println(member.getId());
        return "/home";
    }
}
