package com.example.todo_api.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {

    @Email
    private String email;


    @NotNull
    private String password;
}
