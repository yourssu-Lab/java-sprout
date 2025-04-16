package com.yourssu.roomescape.time;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class TimeDao {
    private final JdbcTemplate jdbcTemplate;

    public TimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Time> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM time WHERE deleted = false",
                (rs, rowNum) -> new Time(
                        rs.getLong("id"),
                        rs.getString("time_value")));
    }

    public Optional<Time> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT * FROM time WHERE id = ? AND deleted = false",
                    (rs, rowNum) -> new Time(
                            rs.getLong("id"),
                            rs.getString("time_value")
                    ),
                    id
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Time save(Time time) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO time(time_value) VALUES (?)", new String[]{"id"});
            ps.setString(1, time.getValue());
            return ps;
        }, keyHolder);

        return new Time(keyHolder.getKey().longValue(), time.getValue());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("UPDATE time SET deleted = true WHERE id = ?", id);
    }
}
