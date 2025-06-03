package com.example.todo_api.member;

import com.example.todo_api.bean.JwtProvider;
import com.example.todo_api.common.ConflictException;
import com.example.todo_api.common.ForbiddenException;
import com.example.todo_api.common.NotFoundException;
import com.example.todo_api.common.message.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.todo_api.common.message.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long save(String email, String password) {
        if(memberRepository.findByEmail(email).isPresent()) {
            throw new ConflictException(EMAIL_EXIST.getMessage() + email);
        }

        String encodedPassword = passwordEncoder.encode(password);
        return memberRepository.save(new Member(email, encodedPassword)).getId();
    }

    @Transactional
    public String login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND.getMessage() + email));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new ForbiddenException(PASSWORD_WRONG.getMessage());
        }

        return jwtProvider.createToken(email);
    }
}
