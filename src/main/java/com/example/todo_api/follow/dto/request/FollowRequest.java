package com.example.todo_api.follow.dto.request;

import com.example.todo_api.member.Member;
import lombok.Data;

@Data
public class FollowRequest {
    private Long followerId;
    private Long followingId;
}
