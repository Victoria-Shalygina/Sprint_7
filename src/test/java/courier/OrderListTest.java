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
import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.filters(new AllureRestAssured());
    }


    @Step("Получение списка заказов")
    public Response getOrdersList() {
        return given()
                .when()
                .get("/api/v1/orders");
    }


    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что ручка возвращает список заказов")
    public void shouldReturnOrdersList() {

        getOrdersList()
                .then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}
