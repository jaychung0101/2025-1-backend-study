package com.example.todo_api.follow;

import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
public class FollowId implements Serializable {

    private Long follower;
    private Long following;

    public FollowId(Long follower, Long following) {
        this.follower = follower;
        this.following = following;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        FollowId followId = (FollowId) object;
        return Objects.equals(follower, followId.follower) && Objects.equals(following, followId.following);
    }

    @Override
    public int hashCode() {
        return Objects.hash(follower, following);
    }
}
