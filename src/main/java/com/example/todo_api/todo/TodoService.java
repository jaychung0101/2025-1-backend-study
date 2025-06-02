package com.example.todo_api.todo;

import com.example.todo_api.member.Member;
import com.example.todo_api.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createTodo(String content, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다: " + memberId));

        Todo todo = new Todo(content, member);
        todoRepository.save(todo);
        return todo.getId();
    }

    @Transactional(readOnly = true)
    public Todo findTodo(Long todoId) {
        return Optional.ofNullable(todoRepository.findById(todoId))
                .orElseThrow(() -> new RuntimeException("해당 할 일이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Todo findTodoForMember(Long todoId, Long memberId) {
        Todo todo = findTodo(todoId);
        if (!todo.getMember().getId().equals(memberId)) {
            throw new RuntimeException("해당 할 일은 이 회원의 것이 아닙니다.");
        }
        return todo;
    }

    @Transactional(readOnly = true)
    public List<Todo> findMyTodos(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다: " + memberId));

        return todoRepository.findAllByMember(member);
    }

    @Transactional
    public void updateTodo(Long memberId, Long todoId, String newContent) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다: " + memberId));

        Todo todo = todoRepository.findById(todoId);

        if (todo == null) {
            throw new RuntimeException("할 일이 존재하지 않습니다.");
        }

        if (!todo.getMember().getId().equals(member.getId())) {
            throw new RuntimeException("할 일은 내가 만든 할 일만 수정 가능합니다.");
        }

        todo.updateContent(newContent);
    }

    @Transactional
    public void deleteTodo(Long memberId, Long todoId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다: " + memberId));

        Todo todo = todoRepository.findById(todoId);

        if (todo == null) {
            throw new RuntimeException("할 일이 존재하지 않습니다.");
        }

        if (!todo.getMember().getId().equals(member.getId())) {
            throw new RuntimeException("할 일은 내가 만든 할 일만 수정 가능합니다.");
        }

        todoRepository.deleteById(todo.getId());
    }

    @Transactional
    public void toggleCheckTodo(Long memberId, Long todoId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다: " + memberId));

        Todo todo = todoRepository.findById(todoId);
        if (todo == null) {
            throw new RuntimeException("할 일이 존재하지 않습니다.");
        }

        if (!todo.getMember().getId().equals(member.getId())) {
            throw new RuntimeException("할 일은 내가 만든 할 일만 체크/체크 해제 가능합니다.");
        }

        todo.toggleIsChecked();
    }
}
