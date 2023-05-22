package com.example.jmptoboot.repository;

import com.example.jmptoboot.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByusername(String username);
}
