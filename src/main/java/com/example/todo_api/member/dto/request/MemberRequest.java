package com.example.todo_api.member.dto.request;

import lombok.Getter;

@Getter
public class MemberRequest {
    private String email;
    private String password;
}
