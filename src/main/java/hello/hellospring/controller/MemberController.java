package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * 실무에서는 주로 정형화된 컨트롤러, 서비스, 리포지토리 같은 코드는 컴포넌트 스캔을 사용한다.
 * 그리고 정형화 되지 않거나, 상황에 따라 구현 클래스를 변경해야 하면 설정을 통해 스프링 빈으로 등록한다.
* */
//컴포넌트 스캔과 자동 의존관계 설정 : SpringConfig.java 대신 @Service, @Repository를 사용하면 됨
//자바 코드로 직접 스프링 빈 등록하기 : 현재 적용 
@Controller
public class MemberController {

    private final MemberService memberService;
    // MemberController 생성자 => DI의 생성자 주입
    // 외부에서 넣어서 생성 => 의존성 주입
    /*
     DI 방법 3가지
     필드 주입, setter 주입, 생성자 주입
     -> 생성자 주입 권장
     (필드 주입 : 변경해줄 수 없음)
     (setter 주입 : public 으로 노출되어 어디선가 변경될 수 있다.)
     */
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    @PostMapping("members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }

}
