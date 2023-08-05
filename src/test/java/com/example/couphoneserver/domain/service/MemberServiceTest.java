package com.example.couphoneserver.domain.service;

import com.example.couphoneserver.common.exception.MemberException;
import com.example.couphoneserver.domain.MemberStatus;
import com.example.couphoneserver.domain.entity.Member;
import com.example.couphoneserver.dto.auth.LoginRequestDto;
import com.example.couphoneserver.dto.auth.LoginResponseDto;
import com.example.couphoneserver.repository.BrandRepository;
import com.example.couphoneserver.repository.MemberRepository;
import com.example.couphoneserver.repository.StoreRepository;
import com.example.couphoneserver.service.MemberService;
import jdk.jfr.Description;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
//@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BrandRepository brandRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    public void cleanUp() {
        memberRepository.deleteAll();
    }

    @Test
    public void 회원_가입() throws Exception {
        // given
        Member member = Member.builder()
                .name("kim")
                .email("aaa@naver.com")
                .password("1234")
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

    @Rollback

    @Test(expected = MemberException.class)
    public void 회원_중복이름_가입시도하면_예외처리() throws Exception {
        // given
        Member memberKim1 = Member.builder()
                .name("kim")
                .email("aaa@naver.com")
                .password("1234")
                .build();
        Member memberKim2 = Member.builder()
                .name("kim")
                .email("bbb@naver.com")
                .password("1234")
                .build();
        Member memberLee = Member.builder()
                .name("lee")
                .email("ccc@naver.com")
                .password("1234")
                .build();
        memberService.join(memberKim1);
        // when
        // 같은 이름을 가진 회원 가입 시도 시 MemberException error
        memberService.join(memberKim2);
        memberService.join(memberLee); // 이름이 중복되지 않으므로 가입 되어야 함.

        // then
        fail(" 중복 이름 회원 가입 시 예외가 발생해야 합니다. ");
    }

    @Test
    public void 회원_전체조회() throws Exception {
        // given
        memberRepository.deleteAll();
        Member member1 = Member.builder()
                .name("yee")
                .email("aaa@naver.com")
                .password("1234")
                .build();
        Member member2 = Member.builder()
                .name("kim")
                .email("bbb@naver.com")
                .password("1234")
                .build();
        Member member3 = Member.builder()
                .name("lee")
                .email("ccc@naver.com")
                .password("1234")
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
                .name("kim")
                .email("aaa@naver.com")
                .password("1234")
                .build();
        Member member2 = Member.builder()
                .name("yoo")
                .email("bbb@naver.com")
                .password("1234")
                .build();
        Member member3 = Member.builder()
                .name("lee")
                .email("ccc@naver.com")
                .password("1234")
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

    @Test(expected = MemberException.class)
    public void 회원_유효하지_않은_id로_조회하면_예외처리() throws Exception {
        // given
        Member member = Member.builder()
                .name("lee")
                .email("aaa@naver.com")
                .password("1234")
                .build();
        // when
        memberService.join(member);
        // then
        Member findMember = memberService.findOneById(member.getId() + 100);
    }

    @Test
    public void 회원_생성시간_수정시간_올바른지() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 7, 20, 0, 0, 0);
        Member member = Member.builder()
                .name("lee")
                .email("aaa@naver.com")
                .password("1234")
                .build();
        // when
        memberService.join(member);

        Member findMember = memberService.findOneById(member.getId());

        //System.out.println("createdDate = " + member.getCreatedDate());
        //System.out.println("modifiedDate = " + member.getModifiedDate());
        // then
        assertThat(member.getCreatedDate()).isAfter(now);
        assertThat(member.getModifiedDate()).isAfter(now);
    }

    @Test
    @Description("비회원이면 회원 가입을 먼저 수행한 뒤, 로그인 처리 후 토큰을 발급합니다.")
    public void 회원_로그인_요청_시_토큰_발급_및_상태_ACTIVE() throws Exception {
        // given
        String email = "aaa@naver.com";
        String name = "김테스트";
        // when
        LoginRequestDto loginRequest = new LoginRequestDto(email, name);
        if (!memberService.isExistingMember(loginRequest)) {
            memberService.saveByEmailAndName(loginRequest);
        }
        LoginResponseDto loginResponse = memberService.signIn(loginRequest);

        Member member = memberService.findOneById(loginResponse.getMemberId());
        // then
        assertAll(
                () -> assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE),
                () -> assertNotNull(loginResponse.getAccessToken())
        );
    }

    @Test
    public void 이메일로_회원_조회() throws Exception {
        // given
        String email = "aaa@naver.com";
        String name = "김테스트";
        // when
        LoginRequestDto loginRequest = new LoginRequestDto(email, name);
        if (!memberService.isExistingMember(loginRequest)) {
            memberService.saveByEmailAndName(loginRequest);
        }
        LoginResponseDto loginResponse = memberService.signIn(loginRequest);
        Member member = memberService.findOneByEmail(email);
        // then
        assertAll(
                () -> assertThat(member.getEmail()).isEqualTo(email),
                () -> assertThat(member.getName()).isEqualTo(name)
        );
    }
}