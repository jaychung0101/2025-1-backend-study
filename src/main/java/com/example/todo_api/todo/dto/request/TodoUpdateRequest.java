package com.example.todo_api.todo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class TodoUpdateRequest {

    @Length(max = 200, message = "content 길이는 100자 입니다.")
    private String newContent;

    @NotNull
    private Long memberId;
}
