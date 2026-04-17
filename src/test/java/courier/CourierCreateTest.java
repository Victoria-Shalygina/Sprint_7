package courier;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class CourierCreateTest {

    private CourierApi courierApi;
    private String login;
    private String password;
    private int courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.filters(new AllureRestAssured());

        courierApi = new CourierApi();

        login = "user_" + System.currentTimeMillis();
        password = "1234";
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierApi.deleteCourier(login, password);
        }
    }

    @Test
    @DisplayName("Успешное создание курьера")
    public void shouldCreateCourier() {

        Courier courier = new Courier(login, password, "Test");

        courierApi.createCourier(courier)
                .then()
                .statusCode(anyOf(is(201), is(409)));

        courierId = courierApi.loginCourier(
                        new CourierLoginRequest(login, password)
                )
                .then()
                .extract()
                .path("id");
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void shouldNotCreateCourierWithoutPassword() {

        Courier courier = new Courier(login, null, "Test");

        courierApi.createCourier(courier)
                .then()
                .statusCode(400)
                .body("message",
                        equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание дубликата курьера")
    public void shouldNotCreateDuplicateCourier() {

        Courier courier = new Courier(login, password, "Test");

        courierApi.createCourier(courier);

        courierApi.createCourier(courier)
                .then()
                .statusCode(409)
                .body("message",
                        equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}
