package com.yourssu.roomescape.member;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Objects;
import java.util.Optional;

@Repository
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member save(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(
                    "INSERT INTO member(name, email, password, role) VALUES (?, ?, ?, ?)",
                    new String[]{"id"}
            );
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPassword());
            ps.setString(4, member.getRole());
            return ps;
        }, keyHolder);

        return new Member(Objects.requireNonNull(keyHolder.getKey()).longValue(), member.getName(), member.getEmail(), member.getPassword(), member.getRole());
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        return query("SELECT * FROM member WHERE email = ? AND password = ?", email, password);
    }

    public Optional<Member> findByName(String name) {
        return query("SELECT * FROM member WHERE name = ?", name);
    }

    public Optional<Member> findByEmail(String email) {
        return query("SELECT * FROM member WHERE email = ?", email);
    }

    private Optional<Member> query(String sql, Object... args) {
        try {
            Member member = jdbcTemplate.queryForObject(sql,
                    (rs, rowNum) -> new Member(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            null, // password는 조회 안 함
                            rs.getString("role")
                    ), args);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
