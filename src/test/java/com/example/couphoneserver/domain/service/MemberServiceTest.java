package com.example.couphoneserver.domain.service;

import com.example.couphoneserver.domain.MemberGrade;
import com.example.couphoneserver.domain.entity.Member;
import com.example.couphoneserver.repository.MemberRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @After
    public void cleanUp() {
        memberRepository.deleteAll();
    }

    @Test
    public void 회원_가입() throws Exception {
        // given
        Member member = Member.builder()
                .name("kim")
                .email("siam0294@naver.com")
                .grade(MemberGrade.ROLE_MEMBER)
                .build();
        // when
        Long savedId = memberService.join(member);
        // then
        Member findMember = memberRepository.findById(savedId)
                .orElseThrow(() -> new IllegalArgumentException("  주어진 id 와 매칭하는 회원을 찾을 수 없습니다.  "));
        assertEquals(member, findMember);
        assertEquals(member.getName(), findMember.getName());
        assertEquals(member.getEmail(), findMember.getEmail());
    }

    @Test(expected = IllegalStateException.class)
    public void 회원_중복이름_가입시도하면_예외처리() throws Exception {
        // given
        Member memberKim1 = Member.builder()
                .name("kim")
                .email("siam0294@naver.com")
                .grade(MemberGrade.ROLE_MEMBER)
                .build();
        Member memberKim2 = Member.builder()
                .name("kim")
                .email("hh@hh.com")
                .grade(MemberGrade.ROLE_MEMBER)
                .build();
        Member memberLee = Member.builder()
                .name("lee")
                .email("hh@hh.com")
                .grade(MemberGrade.ROLE_ADMIN)
                .build();
        memberService.join(memberKim1);
        // when
        // 같은 이름을 가진 회원 가입 시도 시 IllegalStateException error
        memberService.join(memberKim2);
        memberService.join(memberLee); // 이름이 중복되지 않으므로 가입 되어야 함.

        // then
        fail(" 중복 이름 회원 가입 시 예외가 발생해야 합니다. ");
    }

    @Test
    public void 회원_전체조회() throws Exception {
        // given
        Member member1 = Member.builder()
                .name("lee")
                .email("siam0294@naver.com")
                .grade(MemberGrade.ROLE_MEMBER)
                .build();
        Member member2 = Member.builder()
                .name("lim")
                .email("hh@hh.com")
                .grade(MemberGrade.ROLE_MEMBER)
                .build();
        Member member3 = Member.builder()
                .name("yoo")
                .email("hh@hh.com")
                .grade(MemberGrade.ROLE_ADMIN)
                .build();
        // when
        memberService.join(member1);
        memberService.join(member2);
        memberService.join(member3);

        List<Member> members = memberService.findMembers();
        // then
        assertThat(members.size()).isEqualTo(3);
    }

    @Test
    public void 회원_단건조회() throws Exception {
        // given
        Member member1 = Member.builder()
                .name("lee")
                .email("siam0294@naver.com")
                .grade(MemberGrade.ROLE_MEMBER)
                .build();
        Member member2 = Member.builder()
                .name("lim")
                .email("hh@hh.com")
                .grade(MemberGrade.ROLE_MEMBER)
                .build();
        Member member3 = Member.builder()
                .name("yoo")
                .email("hh@hh.com")
                .grade(MemberGrade.ROLE_ADMIN)
                .build();
        // when
        memberService.join(member1);
        memberService.join(member2);
        memberService.join(member3);

        Member findMember1 = memberService.findOneById(member1.getId());
        Member findMember2 = memberService.findOneById(member2.getId());
        Member findMember3 = memberService.findOneById(member3.getId());

        // then
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);
        assertThat(findMember3).isEqualTo(member3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void 회원_유효하지_않은_id로_조회하면_예외처리() throws Exception {
        // given
        Member member = Member.builder()
                .name("lee")
                .email("siam0294@naver.com")
                .grade(MemberGrade.ROLE_MEMBER)
                .build();
        // when
        memberService.join(member);
        // then
        Member findMember = memberService.findOneById(member.getId()+100);
    }
    @Test
    public void 회원_생성시간_수정시간_올바른지() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 7, 20, 0, 0, 0);
        Member member = Member.builder()
                .name("lee")
                .email("siam0294@naver.com")
                .grade(MemberGrade.ROLE_MEMBER)
                .build();
        // when
        memberService.join(member);
        Member findMember = memberService.findOneById(member.getId());

        // System.out.println("createdDate = " + member.getCreatedDate());
        // System.out.println("modifiedDate = " + member.getModifiedDate());
        // then
        assertThat(member.getCreatedDate()).isAfter(now);
        assertThat(member.getModifiedDate()).isAfter(now);
    }
}