package com.yourssu.roomescape.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT m FROM Member m WHERE m.email = :email AND m.password = :password")

    Member findByEmailAndPassword(String email, String password);

    Member findByName(String name);

    Member findByEmail(String email);
}
