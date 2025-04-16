package com.yourssu.roomescape;

import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberDao;
import com.yourssu.roomescape.reservation.ReservationResponse;
import com.yourssu.roomescape.util.JwtTokenProvider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    @Test
    void 일단계() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "admin@email.com");
        params.put("password", "password");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract();

        String token = response.headers().get("Set-Cookie").getValue().split(";")[0].split("=")[1];

        assertThat(token).isNotBlank();

        ExtractableResponse<Response> checkResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract();

        assertThat(checkResponse.body().jsonPath().getString("name")).isEqualTo("어드민");
    }

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MemberDao memberDao;

    private String createToken(String email, String password) {
        // 테스트 DB에 있는 회원 조회
        Member member = memberDao.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new RuntimeException("테스트 회원이 존재하지 않습니다."));

        return jwtTokenProvider.createToken(member);
    }

    @Test
    void 이단계() {
        String token = createToken("admin@email.com", "password");  // 일단계에서 토큰을 추출하는 로직을 메서드로 따로 만들어서 활용하세요.

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", "1");
        params.put("theme", "1");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.as(ReservationResponse.class).getName()).isEqualTo("어드민");

        params.put("name", "브라운");

        ExtractableResponse<Response> adminResponse = RestAssured.given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .extract();

        assertThat(adminResponse.statusCode()).isEqualTo(201);
        assertThat(adminResponse.as(ReservationResponse.class).getName()).isEqualTo("브라운");
    }
}

//    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//    public class MissionStepTest {
//        @Test
//        void 삼단계() {
//            String brownToken = createToken("brown@email.com", "password");
//
//            RestAssured.given().log().all()
//                    .cookie("token", brownToken)
//                    .get("/admin")
//                    .then().log().all()
//                    .statusCode(401);
//
//            String adminToken = createToken("admin@email.com", "password");
//
//            RestAssured.given().log().all()
//                    .cookie("token", adminToken)
//                    .get("/admin")
//                    .then().log().all()
//                    .statusCode(200);
//        }
//    }
