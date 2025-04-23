package com.yourssu.roomescape.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmailAndPassword(String email, String password);

    Member findByName(String name);

    Member findByEmail(String email);
}
