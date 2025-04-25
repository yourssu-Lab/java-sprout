package com.yourssu.roomescape;

import com.yourssu.roomescape.member.MemberRepository;
import com.yourssu.roomescape.reservation.ReservationResponse;
import com.yourssu.roomescape.util.JwtTokenProvider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private MemberRepository memberRepository;

    private String createToken(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password)
                .map(jwtTokenProvider::createToken)
                .orElseThrow(() -> new RuntimeException("해당 테스트 유저가 존재하지 않습니다."));
    }

    @Test
    void 일단계() {
        Map<String, String> params = Map.of("email", "admin@email.com", "password", "password");

        var response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .post("/login")
                .then().log().all()
                .statusCode(200)
                .extract();

        String token = response.header("Set-Cookie").split(";")[0].split("=")[1];

        assertThat(token).isNotBlank();

        var checkResponse = RestAssured.given().log().all()
                .cookie("token", token)
                .get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract();

        assertThat(checkResponse.body().jsonPath().getString("name")).isEqualTo("어드민");
    }

    @Test
    void 이단계() {
        String token = createToken("admin@email.com", "password");

        Map<String, String> params = Map.of("date", "2024-03-01", "time", "1", "theme", "1");

        var response = RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.as(ReservationResponse.class).getName()).isEqualTo("어드민");

        var brownReservation = RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(new HashMap<>(params) {{
                    put("name", "브라운");
                }})
                .post("/reservations")
                .then().log().all()
                .extract();

        assertThat(brownReservation.statusCode()).isEqualTo(201);
        assertThat(brownReservation.as(ReservationResponse.class).getName()).isEqualTo("브라운");
    }

    @Test
    void 삼단계() {
        String brownToken = createToken("brown@email.com", "password");

        RestAssured.given().log().all()
                .cookie("token", brownToken)
                .get("/admin")
                .then().log().all()
                .statusCode(401);

        String adminToken = createToken("admin@email.com", "password");

        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .get("/admin")
                .then().log().all()
                .statusCode(200);
    }

//    @Test
//    void 오단계() {
//        String adminToken = createToken("admin@email.com", "password");
//
//        List<MyReservationResponse> reservations = RestAssured.given().log().all()
//                .cookie("token", adminToken)
//                .get("/reservations-mine")
//                .then().log().all()
//                .statusCode(200)
//                .extract().jsonPath().getList(".", MyReservationResponse.class);
//
//        assertThat(reservations).hasSize(3);
//    }
}
