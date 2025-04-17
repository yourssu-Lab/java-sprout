package com.yourssu.roomescape;

import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberRepository;
import com.yourssu.roomescape.reservation.Reservation;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.theme.ThemeRepository;
import com.yourssu.roomescape.time.Time;
import com.yourssu.roomescape.time.TimeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class MissionJpaTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 사단계_Time() {
        Time time = new Time("10:00");
        entityManager.persist(time);
        entityManager.flush();

        Time persistTime = timeRepository.findById(time.getId()).orElse(null);

        assertThat(persistTime.getValue()).isEqualTo(time.getValue());
    }

    @Test
    void 사단계_Theme() {
        Theme theme = new Theme("name", "description");
        entityManager.persist(theme);
        entityManager.flush();

        Theme persistTheme = themeRepository.findById(theme.getId()).orElse(null);

        assertThat(persistTheme.getName()).isEqualTo(theme.getName());
    }

    @Test
    void 사단계_Member() {
        Member member = new Member("name", "email", "password", "role");
        entityManager.persist(member);
        entityManager.flush();

        Member persistMember = memberRepository.findById(member.getId()).orElse(null);

        assertThat(persistMember.getName()).isEqualTo(member.getName());
    }
}
