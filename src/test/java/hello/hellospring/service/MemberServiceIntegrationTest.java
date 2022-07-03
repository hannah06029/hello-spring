package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*
 스프링 컨테이너와 DB까지 연결한 통합 테스트
 가급적이면 스프링 컨테이너를 사용하지 않는 순수한 단위테스트가 더 좋은 테스트일 확률이 높다 -> MemberServiceTest.java
 -> 항상은 아니지만 확률이 높다.
*/

/*
* @SpringBootTest : 스프링 컨테이너와 테스트를 함께 실행한다.
* @Transactional : 테스트 케이스에 이 애노테이션이 있으면, 테스트 시작 전에 트랜잭션을 시작하고, 테스트 완료 후에 항상 롤백한다.
*                  이렇게 하면 DB에 데이터가 남지 않으므로 다음 테스트에 영향을 주지 않는다.
*/
@SpringBootTest
@Transactional //주석처리하면 db에 저장됨 -> 이 어노테이션을 통해 롤백 (테스트 반복 실행 가능)
class MemberServiceIntegrationTest {

    // test는 injection에서 쓰고 끝이므로 필드 DI가 편함
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    void 회원가입() {
        // given
        Member member = new Member();
        member.setName("spring");

        //when
        Long saveId = memberService.join(member);

        //then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외() {
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);

        //방법1 try~catch
/*
        try{
            memberService.join(member2);
            fail("실패가 되야함.");
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        }
*/

        // 방법2 assertThrows
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));
//        assertThrows(NullPointerException.class, () -> memberService.join(member2)); // 테스트 실패
        // 메세지 검증
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

        //then

    }

}
