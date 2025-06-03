package com.example.todo_api.common;

public class ConflictException extends RuntimeException{
    public ConflictException(String message) {
        super(message);
    }
}
