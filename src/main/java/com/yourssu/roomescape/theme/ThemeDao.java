package com.yourssu.roomescape.theme;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ThemeDao {
    private final JdbcTemplate jdbcTemplate;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Theme> findAll() {
        return jdbcTemplate.query("SELECT * FROM theme where deleted = false", (rs, rowNum) -> new Theme(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description")
        ));
    }

    public Optional<Theme> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT * FROM theme WHERE id = ? AND deleted = false",
                    (rs, rowNum) -> new Theme(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description")
                    ),
                    id
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Theme save(Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement("INSERT INTO theme(name, description) VALUES (?, ?)", new String[]{"id"});
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDescription());
            return ps;
        }, keyHolder);

        return new Theme(Objects.requireNonNull(keyHolder.getKey()).longValue(), theme.getName(), theme.getDescription());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("UPDATE theme SET deleted = true WHERE id = ?", id);
    }
}
