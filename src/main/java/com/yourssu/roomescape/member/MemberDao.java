package com.yourssu.roomescape.member;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;

    // Create a reusable RowMapper for Member objects
    private static final RowMapper<Member> MEMBER_ROW_MAPPER = (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("role")
    );

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member save(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement("INSERT INTO member(name, email, password, role) VALUES (?, ?, ?, ?)", new String[]{"id"});
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPassword());
            ps.setString(4, member.getRole());
            return ps;
        }, keyHolder);

        return new Member(keyHolder.getKey().longValue(), member.getName(), member.getEmail(), "USER");
    }

    public Member findByEmailAndPassword(String email, String password) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT id, name, email, role FROM member WHERE email = ? AND password = ?",
                    MEMBER_ROW_MAPPER,
                    email, password
            );
        } catch (EmptyResultDataAccessException e) {
            return null; // No member found with the given email and password
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new RuntimeException("Multiple members found with the same email and password", e);
        }
    }

    public Member findByName(String name) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT id, name, email, role FROM member WHERE name = ?",
                    MEMBER_ROW_MAPPER,
                    name
            );
        } catch (EmptyResultDataAccessException e) {
            return null; // No member found with the given name
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new RuntimeException("Multiple members found with the same name", e);
        }
    }

    public Member findByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT id, name, email, role FROM member WHERE email = ?",
                    MEMBER_ROW_MAPPER,
                    email
            );
        } catch (EmptyResultDataAccessException e) {
            return null; // No member found with the given email
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new RuntimeException("Multiple members found with the same email", e);
        }
    }
}