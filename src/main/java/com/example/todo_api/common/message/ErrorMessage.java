package com.example.todo_api.common.message;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    MEMBER_NOT_FOUND("존재하지 않는 회원입니다: "),
    TODO_NOT_FOUND("할 일이 존재하지 않습니다: "),
    TODO_NOT_OWNER("해당 할 일은 이 회원의 것이 아닙니다: "),
    EMAIL_NOT_FOUND("존재하지 않는 이메일입니다."),
    EMAIL_EXIST("이미 존재하는 이메일입니다."),
    PASSWORD_WRONG("비밀번호가 일치하지 않습니다."),
    RETRIEVE_NON_FRIEND("친구가 아닌 사람의 할 일을 조회할 수 없습니다.")
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
