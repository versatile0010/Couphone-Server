package com.example.couphoneserver.dto.member;

import com.example.couphoneserver.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    @Schema(example = "1", description = "회원 아이디")
    private Long id;
    public MemberResponseDto(Member member) {
        id = member.getId();
    }
}
