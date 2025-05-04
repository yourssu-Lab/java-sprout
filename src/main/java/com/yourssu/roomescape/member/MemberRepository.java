package com.yourssu.roomescape.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndPassword(String email, String password);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByName(String name);
    boolean existsByEmail(String email);
}
