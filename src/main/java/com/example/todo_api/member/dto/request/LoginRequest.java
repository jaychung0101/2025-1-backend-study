package com.example.todo_api.member.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
