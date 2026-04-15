package courier;

import io.qameta.allure.Step;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreateTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }


    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }


    @Test
    @DisplayName("Успешное создание курьера")
    @Description("Проверка, что курьер создаётся с валидными данными")
    public void shouldCreateCourier() {

        String login = "user" + System.currentTimeMillis();
        Courier courier = new Courier(login, "1234", "Test");

        createCourier(courier)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка, что без пароля возвращается ошибка 400")
    public void shouldNotCreateCourierWithoutPassword() {

        String login = "user" + System.currentTimeMillis();
        Courier courier = new Courier(login, null, "Test");

        createCourier(courier)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание дубликата курьера")
    @Description("Проверка, что нельзя создать двух курьеров с одинаковыми данными")
    public void shouldNotCreateDuplicateCourier() {

        String login = "user" + System.currentTimeMillis();
        Courier courier = new Courier(login, "1234", "Test");

        createCourier(courier);

        createCourier(courier)
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}
