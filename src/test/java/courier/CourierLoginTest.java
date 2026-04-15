package courier;

import io.qameta.allure.Step;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CourierLoginTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.filters(new AllureRestAssured());
    }


    @Step("Создание курьера")
    public String createCourier() {
        String login = "user" + System.currentTimeMillis();

        Courier courier = new Courier(login, "1234", "Test");

        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");

        return login;
    }

    @Step("Логин курьера")
    public Response loginCourier(String login, String password) {
        return given()
                .header("Content-type", "application/json")
                .body("{ \"login\": \"" + login + "\", \"password\": \"" + password + "\" }")
                .when()
                .post("/api/v1/courier/login");
    }


    @Test
    @DisplayName("Успешный логин курьера")
    @Description("Проверка, что курьер может авторизоваться с валидными данными")
    public void shouldLoginCourier() {

        String login = createCourier();

        loginCourier(login, "1234")
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("Проверка, что при неверном пароле возвращается ошибка 404")
    public void shouldNotLoginWithWrongPassword() {

        String login = createCourier();

        loginCourier(login, "wrong")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Логин без пароля")
    @Description("Проверка, что без пароля возвращается ошибка 400")
    public void shouldNotLoginWithoutPassword() {

        String login = createCourier();

        given()
                .header("Content-type", "application/json")
                .body("{ \"login\": \"" + login + "\" }")
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(anyOf(is(400), is(504)));
    }

    @Test
    @DisplayName("Логин несуществующего курьера")
    @Description("Проверка, что несуществующий пользователь не может войти")
    public void shouldNotLoginNonExistingCourier() {

        loginCourier("user" + System.currentTimeMillis(), "1234")
                .then()
                .statusCode(404);
    }
}
