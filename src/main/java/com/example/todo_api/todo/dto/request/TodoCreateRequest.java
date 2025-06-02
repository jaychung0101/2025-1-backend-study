package com.example.todo_api.todo.dto.request;

import lombok.Getter;

@Getter
public class TodoCreateRequest {
    private String content;
    private Long memberId;
}
