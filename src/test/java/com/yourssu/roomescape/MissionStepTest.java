package com.yourssu.roomescape;

import com.yourssu.roomescape.auth.AuthService;
import com.yourssu.roomescape.auth.LoginRequest;
import com.yourssu.roomescape.reservation.ReservationFindAllResponse;
import com.yourssu.roomescape.reservation.ReservationSaveResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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

    @Autowired private AuthService authService;

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

    @Test
    void 이단계() {
        LoginRequest loginRequest = new LoginRequest("admin@email.com", "password");
        String token = authService.login(loginRequest);

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", "1");
        params.put("theme", "1");
        params.put("status", "RESERVED");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.as(ReservationSaveResponse.class).name()).isEqualTo("어드민");

        params.put("name", "브라운");

        ExtractableResponse<Response> adminResponse = RestAssured.given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .extract();

        assertThat(adminResponse.statusCode()).isEqualTo(201);
        assertThat(adminResponse.as(ReservationSaveResponse.class).name()).isEqualTo("브라운");
    }

    @Test
    void 삼단계() {
        LoginRequest loginRequest = new LoginRequest("brown@email.com", "password");
        String brownToken = authService.login(loginRequest);

        RestAssured.given().log().all()
                .cookie("token", brownToken)
                .get("/admin")
                .then().log().all()
                .statusCode(401);

        loginRequest = new LoginRequest("admin@email.com", "password");
        String adminToken = authService.login(loginRequest);

        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 오단계() {
        LoginRequest loginRequest = new LoginRequest("admin@email.com", "password");
        String adminToken = authService.login(loginRequest);

        List<ReservationFindAllResponse> reservations = RestAssured.given().log().all()
                .cookie("token", adminToken)
                .get("/reservations/mine")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", ReservationFindAllResponse.class);

        assertThat(reservations).hasSize(3);
    }

    @Test
    void 육단계() {
        LoginRequest loginRequest = new LoginRequest("brown@email.com", "password");
        String brownToken = authService.login(loginRequest);

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", "1");
        params.put("theme", "1");
        params.put("status", "WAITING");

        // 예약 대기 생성
        ReservationSaveResponse waiting = RestAssured.given().log().all()
                .body(params)
                .cookie("token", brownToken)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .statusCode(201)
                .extract().as(ReservationSaveResponse.class);

        // 내 예약 목록 조회
        List<ReservationFindAllResponse> myReservations = RestAssured.given().log().all()
                .body(params)
                .cookie("token", brownToken)
                .contentType(ContentType.JSON)
                .get("/reservations/mine")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", ReservationFindAllResponse.class);

        // 예약 대기 상태 확인
        String status = myReservations.stream()
                .filter(it -> it.reservationId().equals(waiting.id()))
                .filter(it -> !it.status().equals("예약"))
                .findFirst()
                .map(ReservationFindAllResponse::status)
                .orElse(null);

        assertThat(status).isEqualTo("2번째 예약대기");
    }
}
