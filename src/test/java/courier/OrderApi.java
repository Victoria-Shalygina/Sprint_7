package courier;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderApi {

    private static final String BASE_PATH = "/api/v1/orders";

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(BASE_PATH);
    }

    @Step("Получение списка заказов")
    public Response getOrders() {
        return given()
                .get(BASE_PATH);
    }
}
