package com.example.todo_api.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void findByEmail() {
        Member member = new Member("example@google.com", "abcd");
        memberRepository.save(member);

        memberRepository.flush();

        Optional<Member> byEmail = memberRepository.findByEmail("example@google.com");
        Assertions.assertThat(byEmail).isPresent();
        Assertions.assertThat(byEmail.get().getEmail()).isEqualTo(member.getEmail());
    }

    @AfterAll
    public static void doNotFinish() {
        while(true) {}
    }
}