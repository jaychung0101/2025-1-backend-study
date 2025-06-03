package com.example.todo_api.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MemberRequest {

    @Email(message = "올바른 이메일 형식을 입력해주세요")
    private String email;

    @NotNull
    private String password;
}
