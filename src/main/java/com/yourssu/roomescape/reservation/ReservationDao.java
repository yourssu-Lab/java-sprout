package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberDao;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.time.Time;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        return jdbcTemplate.query(
                "SELECT r.id AS reservation_id, r.name as reservation_name, r.date as reservation_date, " +
                        "t.id AS theme_id, t.name AS theme_name, t.description AS theme_description, " +
                        "ti.id AS time_id, ti.time_value AS time_value " +
                        "FROM reservation r " +
                        "JOIN theme t ON r.theme_id = t.id " +
                        "JOIN time ti ON r.time_id = ti.id",

                (rs, rowNum) -> new Reservation(
                        rs.getLong("reservation_id"),
                        rs.getString("reservation_name"),
                        rs.getString("reservation_date"),
                        new Time(
                                rs.getLong("time_id"),
                                rs.getString("time_value")
                        ),
                        new Theme(
                                rs.getLong("theme_id"),
                                rs.getString("theme_name"),
                                rs.getString("theme_description")
                        )));
    }

    public Reservation save(ReservationRequest reservationRequest, Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String name;
        if (reservationRequest.getName() == null) {
            name = member.getName();
        } else {
            name = reservationRequest.getName();
        }
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO reservation(date, name, theme_id, time_id) VALUES (?, ?, ?, ?)", new String[]{"id"});
            ps.setString(1, reservationRequest.getDate());
            ps.setString(2, reservationRequest.getName());
            ps.setLong(3, reservationRequest.getTheme());
            ps.setLong(4, reservationRequest.getTime());
            return ps;
        }, keyHolder);

        Time time = jdbcTemplate.queryForObject("SELECT * FROM time WHERE id = ?",
                (rs, rowNum) -> new Time(rs.getLong("id"), rs.getString("time_value")),
                reservationRequest.getTime());

        Theme theme = jdbcTemplate.queryForObject("SELECT * FROM theme WHERE id = ?",
                (rs, rowNum) -> new Theme(rs.getLong("id"), rs.getString("name"), rs.getString("description")),
                reservationRequest.getTheme());

        return new Reservation(
                keyHolder.getKey().longValue(),
                reservationRequest.getName(),
                reservationRequest.getDate(),
                time,
                theme
        );
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    public List<Reservation> findReservationsByDateAndTheme(String date, Long themeId) {
        return jdbcTemplate.query(
                "SELECT r.id AS reservation_id, r.name as reservation_name, r.date as reservation_date, " +
                        "t.id AS theme_id, t.name AS theme_name, t.description AS theme_description, " +
                        "ti.id AS time_id, ti.time_value AS time_value " +
                        "FROM reservation r " +
                        "JOIN theme t ON r.theme_id = t.id " +
                        "JOIN time ti ON r.time_id = ti.id" +
                        "WHERE r.date = ? AND r.theme_id = ?",
                new Object[]{date, themeId},
                (rs, rowNum) -> new Reservation(
                        rs.getLong("reservation_id"),
                        rs.getString("reservation_name"),
                        rs.getString("reservation_date"),
                        new Time(
                                rs.getLong("time_id"),
                                rs.getString("time_value")
                        ),
                        new Theme(
                                rs.getLong("theme_id"),
                                rs.getString("theme_name"),
                                rs.getString("theme_description")
                        )));
    }

    public List<Reservation> findByDateAndThemeId(String date, Long themeId) {
        return jdbcTemplate.query(
                "SELECT r.id AS reservation_id, r.name as reservation_name, r.date as reservation_date, " +
                        "t.id AS theme_id, t.name AS theme_name, t.description AS theme_description, " +
                        "ti.id AS time_id, ti.time_value AS time_value " +
                        "FROM reservation r " +
                        "JOIN theme t ON r.theme_id = t.id " +
                        "JOIN time ti ON r.time_id = ti.id " +
                        "WHERE r.date = ? AND r.theme_id = ?",
                new Object[]{date, themeId},
                (rs, rowNum) -> new Reservation(
                        rs.getLong("reservation_id"),
                        rs.getString("reservation_name"),
                        rs.getString("reservation_date"),
                        new Time(
                                rs.getLong("time_id"),
                                rs.getString("time_value")
                        ),
                        new Theme(
                                rs.getLong("theme_id"),
                                rs.getString("theme_name"),
                                rs.getString("theme_description")
                        )));
    }
    public Theme findThemeById(Long themeId) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM theme WHERE id = ?",
                (rs, rowNum) -> new Theme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ),
                themeId
        );
    }

    public Time findTimeById(Long timeId) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM time WHERE id = ?",
                (rs, rowNum) -> new Time(
                        rs.getLong("id"),
                        rs.getString("time_value")
                ),
                timeId
        );
    }
    public Member findMemberByName(String name) {
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

    public Member findMemberById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT id, name, email, role FROM member WHERE id = ?",
                (rs, rowNum) -> new Member(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("role")
                ),
                id
        );
    }

    public Reservation save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO reservation(date, name, theme_id, time_id) VALUES (?, ?, ?, ?)",
                    new String[]{"id"});
            ps.setString(1, reservation.getDate());
            ps.setString(2, reservation.getName());
            ps.setLong(3, reservation.getTheme().getId());
            ps.setLong(4, reservation.getTime().getId());
            return ps;
        }, keyHolder);

        return new Reservation(
                keyHolder.getKey().longValue(),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

}

