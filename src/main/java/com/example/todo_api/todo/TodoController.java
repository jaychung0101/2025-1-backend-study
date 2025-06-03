package com.example.todo_api.todo;

import com.example.todo_api.todo.dto.request.TodoCreateRequest;
import com.example.todo_api.todo.dto.request.TodoUpdateRequest;
import com.example.todo_api.todo.dto.response.TodoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController // @Controller + @ResponseBody
//@Controller
//@ResponseBody // 자바 객체를 JSON 형식으로 변환
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<Void> createTodo(@RequestBody @Valid TodoCreateRequest request) {
        Long todoId = todoService.createTodo(request.getContent(), request.getMemberId());
        return ResponseEntity.created(URI.create("/todo/" + todoId)).build();
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<TodoResponse> getTodo(@RequestParam Long memberId, @PathVariable Long todoId) {
        Todo todo = todoService.findTodoForMember(todoId, memberId);
        return ResponseEntity.ok(new TodoResponse(todo));
    }

    @PatchMapping("/{todoId}")
    public ResponseEntity<Void> updateTodo(@RequestBody TodoUpdateRequest request, @PathVariable Long todoId) {
        todoService.updateTodo(request.getMemberId(), todoId, request.getNewContent());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<TodoResponse>> getTodoList(@RequestParam Long memberId) {
        List<Todo> todos = todoService.findMyTodos(memberId);
        List<TodoResponse> response = todos.stream()
                .map(TodoResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodo(@RequestParam Long memberId, @PathVariable Long todoId) {
        todoService.deleteTodo(memberId, todoId);
        return ResponseEntity.noContent().build(); // 204 no content
    }

    @PatchMapping("/{todoId}/check")
    public ResponseEntity<Void> checkTodo(@RequestParam Long memberId, @PathVariable Long todoId) {
        todoService.toggleCheckTodo(memberId, todoId);
        return ResponseEntity.ok().build();
    }
}
