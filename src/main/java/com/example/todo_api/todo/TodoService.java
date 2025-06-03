package com.example.todo_api.todo;

import com.example.todo_api.common.BadRequestException;
import com.example.todo_api.common.ForbiddenException;
import com.example.todo_api.common.NotFoundException;
import com.example.todo_api.member.Member;
import com.example.todo_api.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.todo_api.common.message.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createTodo(String content, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND.getMessage() + memberId));

        Todo todo = new Todo(content, member);
        todoRepository.save(todo);
        return todo.getId();
    }

    @Transactional(readOnly = true)
    public Todo findTodo(Long todoId) {
        return Optional.ofNullable(todoRepository.findById(todoId))
                .orElseThrow(() -> new NotFoundException(TODO_NOT_FOUND.getMessage() + todoId));
    }

    @Transactional(readOnly = true)
    public Todo findTodoForMember(Long todoId, Long memberId) {
        Todo todo = findTodo(todoId);
        if (!todo.getMember().getId().equals(memberId)) {
            throw new ForbiddenException(TODO_NOT_OWNER.getMessage() + todoId + ", " + memberId);
        }
        return todo;
    }

    @Transactional(readOnly = true)
    public List<Todo> findMyTodos(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND.getMessage() + memberId));

        return todoRepository.findAllByMember(member);
    }

    @Transactional
    public void updateTodo(Long memberId, Long todoId, String newContent) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND.getMessage() + memberId));

        Todo todo = todoRepository.findById(todoId);

        if (todo == null) {
            throw new NotFoundException(TODO_NOT_FOUND.getMessage());
        }

        if (!todo.getMember().getId().equals(member.getId())) {
            throw new ForbiddenException(TODO_NOT_OWNER.getMessage());
        }

        todo.updateContent(newContent);
    }

    @Transactional
    public void deleteTodo(Long memberId, Long todoId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND.getMessage() + memberId));

        Todo todo = todoRepository.findById(todoId);

        if (todo == null) {
            throw new NotFoundException(TODO_NOT_FOUND.getMessage() + todoId);
        }

        if (!todo.getMember().getId().equals(member.getId())) {
            throw new ForbiddenException(TODO_NOT_OWNER.getMessage() + memberId);
        }

        todoRepository.deleteById(todo.getId());
    }

    @Transactional
    public void toggleCheckTodo(Long memberId, Long todoId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND.getMessage() + memberId));

        Todo todo = todoRepository.findById(todoId);
        if (todo == null) {
            throw new NotFoundException(TODO_NOT_FOUND.getMessage() + todoId);
        }

        if (!todo.getMember().getId().equals(member.getId())) {
            throw new ForbiddenException(TODO_NOT_OWNER.getMessage() + todoId);
        }

        todo.toggleIsChecked();
    }
}
