package com.example.todo_api.follow;

import com.example.todo_api.follow.dto.request.FollowRequest;
import com.example.todo_api.follow.dto.response.Response;
import com.example.todo_api.member.MemberService;
import com.example.todo_api.todo.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {
    private final FollowService followService;

    @GetMapping("/{followerId}/list")
    public ResponseEntity<Response<List<Todo>>> getFriendTodos(@PathVariable("followerId") Long followerId, @RequestParam("followingId") Long followingId) {
        List<Todo> friendTodos = followService.getFriendTodos(followerId, followingId);

        Response<List<Todo>> response = new Response<>(
                true,
                "친구의 할 일 조회 성공",
                friendTodos);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{followerId}/{todoId}")
    public ResponseEntity<Response<Todo>> getFriendTodo(
            @PathVariable("followerId") Long followerId,
            @PathVariable("todoId") Long todoId,
            @RequestParam("followingId") Long followingId){
        Todo todo = followService.getFriendTodo(followerId, followingId, todoId);

        Response<Todo> response = new Response<>(
                true,
                "친구의 할 일 조회 성공",
                todo);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{followerId}/follow")
    public ResponseEntity<Void> followToggle(@PathVariable("followerId") Long followerId, @RequestParam("followingId") Long followingId) {
        followService.followToggle(followerId, followingId);
        return ResponseEntity.ok().build();
    }
}
