package com.example.todo_api.follow;

import com.example.todo_api.follow.Follow;
import com.example.todo_api.follow.FollowRepository;
import com.example.todo_api.member.Member;
import com.example.todo_api.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FollowRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FollowRepository followRepository;

    private Member member1, member2, member3;

    @BeforeEach
    public void saveMembers() {
        member1 = new Member();
        member2 = new Member();
        member3 = new Member();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
    }

    @Test
    @Transactional
    void followSaveTest(){
        Follow follow = new Follow(member1, member2);

        followRepository.save(follow);

        Assertions.assertThat(follow.getId()).isNotNull();
    }

    @Test
    @Transactional
    void followFindOneByIdTest() {
        Follow follow = new Follow(member1, member2);

        followRepository.save(follow);

        followRepository.flushAndClear();

        Follow findFollow = followRepository.findById(follow.getId());

        Assertions.assertThat(findFollow.getId()).isEqualTo(follow.getId());
    }

    @Test
    @Transactional
    void followFindAllTest() {
        Follow follow1 = new Follow(member1, member2);
        Follow follow2 = new Follow(member1, member2);

        followRepository.save(follow1);
        followRepository.save(follow2);

        List<Follow> findAll = followRepository.findAll();

        Assertions.assertThat(findAll).hasSize(2);
    }

    @Test
    @Transactional
    void followFindByFollowerTest() {
        Follow follow1 = new Follow(member1, member2);
        Follow follow2 = new Follow(member2, member3);
        Follow follow3 = new Follow(member1, member3);

        followRepository.save(follow1);
        followRepository.save(follow2);
        followRepository.save(follow3);

        List<Follow> allByFollower = followRepository.findAllByFollower(member1);

        Assertions.assertThat(allByFollower).hasSize(2);
    }

    @Test
    @Transactional
    void followFindByFollowingTest() {
        Follow follow1 = new Follow(member1, member2);
        Follow follow2 = new Follow(member2, member3);
        Follow follow3 = new Follow(member1, member3);

        followRepository.save(follow1);
        followRepository.save(follow2);
        followRepository.save(follow3);

        List<Follow> allByFollowing = followRepository.findAllByFollowing(member3);

        Assertions.assertThat(allByFollowing).hasSize(2);
    }

    @Test
    @Transactional
    void followDeleteById() {
        Follow follow = new Follow(member1, member2);
        followRepository.save(follow);

        followRepository.flushAndClear();

        followRepository.deleteById(follow.getId());

        Follow byId = followRepository.findById(follow.getId());
        Assertions.assertThat(byId).isNull();
    }

    @Test
    @Transactional
    void followDeleteByFollower() {
        Follow follow = new Follow(member1, member2);
        followRepository.save(follow);

        followRepository.flushAndClear();

        followRepository.deleteByFollower(member1);

        Follow byId = followRepository.findById(follow.getId());
        Assertions.assertThat(byId).isNull();
    }

    @AfterAll
    public static void doNotFinish() {
        System.out.println("test finished");

        while (true) {}
    }
}
