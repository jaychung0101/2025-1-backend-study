package com.example.todo_api.follow;

import com.example.todo_api.common.ForbiddenException;
import com.example.todo_api.common.NotFoundException;
import com.example.todo_api.common.message.ErrorMessage;
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

import static com.example.todo_api.common.message.ErrorMessage.*;

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
            throw new ForbiddenException(RETRIEVE_NON_FRIEND.getMessage());
        }

        Todo todo = todoRepository.findById(todoId);
        if (todo == null) {
            throw new NotFoundException(TODO_NOT_FOUND.getMessage() + todoId);
        }

        return todo;
    }

    @Transactional
    public List<Todo> getFriendTodos(Long followerId, Long followingId) {
        Member follower = getMemberOrThrow(followerId);
        Member following = getMemberOrThrow(followingId);

        if (isNotMutualFollow(follower, following)) {
            throw new ForbiddenException(RETRIEVE_NON_FRIEND.getMessage());
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
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND.getMessage() + id));
    }
}
