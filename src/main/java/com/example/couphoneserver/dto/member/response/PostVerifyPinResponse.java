package com.example.couphoneserver.dto.member.response;

import com.example.couphoneserver.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostVerifyPinResponse {
    @Schema(example = "1", description = "회원 아이디")
    private Long id;

    public PostVerifyPinResponse(Member member) {
        id = member.getId();
    }
}
