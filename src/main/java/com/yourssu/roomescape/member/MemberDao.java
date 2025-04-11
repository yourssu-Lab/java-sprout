package com.yourssu.roomescape.member;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {
    private JdbcTemplate jdbcTemplate;

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
        // 실행 당시 로그인 안한상태면 EmptyResultDataAccessException 발생해 예외처리 발생했는데,
        // 쿼리 결과가 2개 이상이면 IncorrectResultSizeDataAccessException 뜰듯
        // ExceptionController에서 일단 예외처리하는듯?
        return jdbcTemplate.queryForObject(
                "SELECT id, name, email, role FROM member WHERE email = ? AND password = ?",
                (rs, rowNum) -> new Member(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("role")
                ),
                email, password
        );
    }

    public Member findByName(String name) {
        return jdbcTemplate.queryForObject(
                "SELECT id, name, email, role FROM member WHERE name = ?",
                (rs, rowNum) -> new Member(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("role")
                ),
                name
        );
    }

    public Member findByEmail(String email) {
        return jdbcTemplate.queryForObject(
                "SELECT id, name, email, role FROM member WHERE email = ?",
                (rs, rowNum) -> new Member(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("role")
                ),
                email
        );
    }
}
