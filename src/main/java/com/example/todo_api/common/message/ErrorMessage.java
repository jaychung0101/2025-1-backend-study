package com.example.todo_api.common.message;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    MEMBER_NOT_FOUND("존재하지 않는 회원입니다: "),
    TODO_NOT_FOUND("할 일이 존재하지 않습니다: "),
    TODO_NOT_OWNER("해당 할 일은 이 회원의 것이 아닙니다: ")
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
