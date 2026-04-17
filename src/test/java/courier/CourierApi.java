package courier;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class CourierApi {

    @Step("Создать курьера")
    public Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Логин курьера")
    public Response loginCourier(CourierLoginRequest request) {
        return given()
                .header("Content-type", "application/json")
                .body(request)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Удалить курьера")
    public void deleteCourier(String login, String password) {

        Response loginResponse = loginCourier(
                new CourierLoginRequest(login, password)
        );
        
        if (loginResponse.statusCode() != 200) {
            return;
        }

        int id = loginResponse.jsonPath().getInt("id");

        given()
                .delete("/api/v1/courier/" + id)
                .then()
                .statusCode(anyOf(is(200), is(202), is(204)));
    }
}
