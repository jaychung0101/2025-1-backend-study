package com.example.todo_api.member;

import com.example.todo_api.member.dto.request.LoginRequest;
import com.example.todo_api.member.dto.request.MemberRequest;
import com.example.todo_api.member.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody MemberRequest member) {
        Long id = memberService.save(member.getEmail(), member.getPassword());
        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = memberService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam String token) {
        return ResponseEntity.ok().build();
    }
}
