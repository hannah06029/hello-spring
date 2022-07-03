package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//jpa를 사용할때 @Transactional 필요
//JPA를 통한 모든 데이터 변경은 트랜잭션 안에서 실행해야 한다.
@Transactional
public class MemberService {

    // MemberService와 MemberServiceTest의 객체를 같게 해줘야 같은 DB 데이터를 이용할 수 있다.
    // private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final MemberRepository memberRepository;
    // MemberService 생성자
    // 외부에서 넣어서 생성 => 의존성 주입
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
    * 회원 가입
    */
    public Long join(Member member) {
        // 같은 이름이 있는 중복 회원X
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

}
