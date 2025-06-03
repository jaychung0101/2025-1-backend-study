package com.example.todo_api.follow;

import com.example.todo_api.member.Member;
import com.example.todo_api.member.MemberRepository;
import com.example.todo_api.member.MemberService;
import com.example.todo_api.todo.Todo;
import com.example.todo_api.todo.TodoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void followToggle(Long followerId, Long followingId) {

        Member follower = getMemberOrThrow(followerId);
        Member following = getMemberOrThrow(followingId);

        if (follower.equals(following)) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }

        Follow existing = followRepository.findByFollowerAndFollowing(follower, following);

        if (existing != null) {
            followRepository.deleteById(existing.getId());
        } else {
            followRepository.save(new Follow(follower, following));
        }
    }

    @Transactional(readOnly = true)
    public Todo getFriendTodo(Long followerId, Long followingId, Long todoId) {
        Member follower = getMemberOrThrow(followerId);
        Member following = getMemberOrThrow(followingId);

        if (isNotMutualFollow(follower, following)) {
            throw new RuntimeException("친구가 아닌 사람의 할 일을 조회할 수 없습니다.");
        }

        Todo todo = todoRepository.findById(todoId);
        if (todo == null) {
            throw new RuntimeException("할 일이 존재하지 않습니다.");
        }

        return todo;
    }

    @Transactional
    public List<Todo> getFriendTodos(Long followerId, Long followingId) {
        Member follower = getMemberOrThrow(followerId);
        Member following = getMemberOrThrow(followingId);

        if (isNotMutualFollow(follower, following)) {
            throw new RuntimeException("친구가 아닌 사람의 할 일을 조회할 수 없습니다.");
        }

        return todoRepository.findAllByMember(following);
    }

    private boolean isNotMutualFollow(Member a, Member b) {
        return !(
                followRepository.findByFollowerAndFollowing(a, b) != null &&
                followRepository.findByFollowerAndFollowing(b, a) != null
        );
    }

    private Member getMemberOrThrow(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));
    }
}
