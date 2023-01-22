package ordertest;

import ingredient.IngredientClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.Order;
import order.OrderClient;
import org.junit.Test;
import user.User;
import user.UserClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@DisplayName("Получение заказов конкретного пользователя")
public class OrderListTest {

    private Order order;
    private final OrderClient orderClient = new OrderClient();
    private final UserClient userClient = new UserClient();
    private String accessToken;
    IngredientClient ingredientClient = new IngredientClient();
    ValidatableResponse response;

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getOrderListAuthTest() {
        User user = User.getRandomUser();
        accessToken = userClient.create(user).extract().path("accessToken");
        order = Order.random(ingredientClient.getIngredients(), 3);
        orderClient.createOrder(order, accessToken);
        orderClient.createOrder(order, accessToken);
        orderClient.createOrder(order, accessToken);
        response = orderClient.getOrders(accessToken);
        userClient.deleteUser(accessToken);
        response.and().assertThat().body("orders", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void getOrderListNotAuthTest() {
        accessToken = "";
        order = Order.random(ingredientClient.getIngredients(), 3);
        orderClient.createOrder(order, accessToken);
        orderClient.createOrder(order, accessToken);
        orderClient.createOrder(order, accessToken);
        response = orderClient.getOrders(accessToken);
        response.and().assertThat().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }
}
