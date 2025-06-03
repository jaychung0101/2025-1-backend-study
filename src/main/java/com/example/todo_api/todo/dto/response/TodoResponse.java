package com.example.todo_api.todo.dto.response;

import com.example.todo_api.todo.Todo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoResponse {
    private Long id;
    private String content;
    private boolean checked;

    public TodoResponse(Todo todo) {
        this.id = todo.getId();
        this.content = todo.getContent();
        this.checked = todo.isChecked();
    }
}
