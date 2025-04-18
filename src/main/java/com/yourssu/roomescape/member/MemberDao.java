    package com.yourssu.roomescape.member;

    import com.yourssu.roomescape.exception.CustomException;
    import com.yourssu.roomescape.exception.ErrorCode;
    import org.springframework.dao.DataAccessException;
    import org.springframework.jdbc.core.JdbcTemplate;
    import org.springframework.jdbc.core.RowMapper;
    import org.springframework.jdbc.support.GeneratedKeyHolder;
    import org.springframework.jdbc.support.KeyHolder;
    import org.springframework.stereotype.Repository;

    @Repository
    public class MemberDao {
        private JdbcTemplate jdbcTemplate;

        public MemberDao(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        private final RowMapper<Member> memberRowMapper = (rs, rowNum) -> new Member(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("role")
        );

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
                        memberRowMapper,
                        email, password
                );
            } catch (DataAccessException e) {
                throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
            }
        }

        public Member findByEmail(String email) {
            try {
                return jdbcTemplate.queryForObject(
                        "SELECT id, name, email, role FROM member WHERE email = ?",
                        memberRowMapper,
                        email
                );
            } catch (DataAccessException e) {
                throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
            }
        }

        public Member findByName(String name) {
            try {
                return jdbcTemplate.queryForObject(
                        "SELECT id, name, email, role FROM member WHERE name = ?",
                        memberRowMapper,
                        name
                );
            } catch (DataAccessException e) {
                throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
            }
        }
    }
