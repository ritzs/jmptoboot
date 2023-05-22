package com.example.jmptoboot.service;

import com.example.jmptoboot.constant.UserRole;
import com.example.jmptoboot.entity.Member;
import com.example.jmptoboot.exception.DataNotFoundException;
import com.example.jmptoboot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = this.memberRepository.findByusername(username);
        member.orElseThrow(() ->new DataNotFoundException("사용자를 찾을 수 없습니다."));


        List<GrantedAuthority> authorities = new ArrayList<>();

        if ("admin".equals(member.get().getUsername())) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }

        return new User(member.get().getUsername(), member.get().getPassword(), authorities);
    }
}