package com.example.couphoneserver.dto.member;

import com.example.couphoneserver.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddMemberRequest {
    @NotNull
    @Size(min=3, max=100)
    @Schema(example = "김이름")
    private String name;
    @NotNull
    @Size(min=3, max=100)
    @Schema(example = "010-1234-5678")
    private String phoneNumber;
    @NotNull
    @Size(min=3, max=100)
    @Schema(example = "1234!@#$")
    private String password;

    public Member toEntity(){
        return Member.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .password(password)
                .build();
    }
}