package com.example.jmptoboot.service;

import com.example.jmptoboot.dto.UserCreateFormVo;
import com.example.jmptoboot.entity.Member;
import com.example.jmptoboot.exception.DataNotFoundException;
import com.example.jmptoboot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(@RequestParam UserCreateFormVo request){
        Member member = Member.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword1()))
                .build();
        this.memberRepository.save(member);
        return member;
    }

    public Member getUser(String username) {
        Optional<Member> member = this.memberRepository.findByusername(username);
        return member.orElseThrow(() -> new DataNotFoundException("사용자 없음"));
    }

}