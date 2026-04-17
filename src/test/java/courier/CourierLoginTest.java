package courier;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class CourierLoginTest {

    private CourierApi courierApi;
    private String login;
    private String password;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.filters(new AllureRestAssured());

        courierApi = new CourierApi();

        login = "user_" + System.currentTimeMillis();
        password = "1234";

        Courier courier = new Courier(login, password, "Test");

        courierApi.createCourier(courier);
    }

    @After
    public void tearDown() {
        courierApi.deleteCourier(login, password);
    }

    @Test
    @DisplayName("Успешный логин курьера")
    public void shouldLoginCourier() {

        courierApi.loginCourier(
                        new CourierLoginRequest(login, password))
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    public void shouldNotLoginWithWrongPassword() {

        courierApi.loginCourier(
                        new CourierLoginRequest(login, "wrong"))
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин без пароля")
    public void shouldNotLoginWithoutPassword() {

        courierApi.loginCourier(
                        new CourierLoginRequest(login, null))
                .then()
                .statusCode(anyOf(is(400), is(504)));
    }

    @Test
    @DisplayName("Логин несуществующего курьера")
    public void shouldNotLoginNonExistingCourier() {

        courierApi.loginCourier(
                        new CourierLoginRequest("fake_" + System.currentTimeMillis(), "1234"))
                .then()
                .statusCode(404);
    }
}
