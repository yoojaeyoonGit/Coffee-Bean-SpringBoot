package jpabook.jpashop.controller;
import jpabook.jpashop.SessionConst;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
public class Session {
    private final LoginService loginService;

    @GetMapping("/login")
    public String loginPage(@ModelAttribute("loginForm") LoginForm form, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session == null) {
            return "login";
        }

        Member loginmember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        if (loginmember == null) {
            return "login";
        }
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request) {
        try{
        System.out.println("password = " + form.getPassword() + "   " + "loginId = " + form.getLoginId());
        if (bindingResult.hasErrors()) {
            return "login";
        }

        Member loginMember = loginService.login((Long) form.getLoginId(), form.getPassword());
        log.info("login? {}", loginMember.getName());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "redirect:/login";
        }

        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        System.out.println("session = " + session.getAttribute("loginMember"));
        return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/login";
        }
    }
        @GetMapping("/logout")
        public String logout(HttpServletRequest request) {
            HttpSession session = request.getSession(false);
            if(session != null) {
                session.invalidate();
            }
            return "redirect:/";
        }
}

