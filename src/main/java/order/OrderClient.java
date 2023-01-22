package order;

import configuration.BaseClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class OrderClient extends BaseClient {
    private static final String ORDERS = "/orders";

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order, String accessToken) {
        return getSpec()
                .header("Authorization", accessToken)
                .and()
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().all();
    }

    @Step("Получение заказов конкретного пользователя")
    public ValidatableResponse getOrders(String accessToken) {
        return getSpec()
                .header("Authorization", accessToken)
                .when()
                .get(ORDERS)
                .then().log().all();
    }

    public ValidatableResponse getOrdersWithoutAuth() {
        return getSpec()
                .when()
                .get(ORDERS)
                .then().log().all();
    }


}
