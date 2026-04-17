package courier;

import io.qameta.allure.Step;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final String[] color;

    public OrderCreateTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getColorData() {
        return new Object[][] {
                { new String[] {"BLACK"} },
                { new String[] {"GREY"} },
                { new String[] {"BLACK", "GREY"} },
                { null }
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.filters(new AllureRestAssured());
    }


    @Step("Создание заказа с цветом: {color}")
    public Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders");
    }


    @Test
    @DisplayName("Создание заказа с разными цветами")
    @Description("Проверка, что заказ создаётся с различными параметрами цвета")
    public void shouldCreateOrderWithDifferentColors() {

        Order order = new Order(color);

        createOrder(order)
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}
