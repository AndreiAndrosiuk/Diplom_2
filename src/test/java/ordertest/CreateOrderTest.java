package ordertest;

import ingredient.IngredientClient;
import io.qameta.allure.junit4.DisplayName;
import order.Order;
import order.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;

@DisplayName("Создание заказа")
public class CreateOrderTest {
    User user;
    private Order order;
    private OrderClient ordersClient = new OrderClient();
    private IngredientClient ingredientClient = new IngredientClient();
    private final UserClient userClient = new UserClient();
    private String accessToken;
    private String hash;

    @Before
    public void setUp() {
        ingredientClient = new IngredientClient();
        ordersClient = new OrderClient();
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    public void createOrderWithAuth() {
        User user = User.getRandomUser();
        accessToken = userClient.create(user).extract().path("accessToken");
        order = Order.random(ingredientClient.getIngredients(), 3);
        ordersClient.createOrder(order, accessToken)
                .and()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("order.number", notNullValue())
                .and()
                .body("order.owner.email", is(user.getEmail().toLowerCase()));

    }

    @Test
    @DisplayName("Создание заказа без авторизации и с ингредиентами")
    public void createOrderWithoutAuth() {
        User user = User.getRandomUser();
        accessToken = "";
        order = Order.random(ingredientClient.getIngredients(), 3);
        ordersClient.createOrder(order, accessToken)
                .and()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации и без ингредиентов")
    public void createOrderWithoutAuthAndIngredients() {
        User user = User.getRandomUser();
        accessToken = "";
        order = Order.random(ingredientClient.getIngredients(), 0);
        ordersClient.createOrder(order, accessToken)
                .and()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithInvalidHash() {
        User user = User.getRandomUser();
        accessToken = userClient.create(user).extract().path("accessToken");
        hash = "000000000000000000000000";
        order = Order.withHash(hash);
        ordersClient.createOrder(order, accessToken)
                .and()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", is("One or more ids provided are incorrect"));
    }

    @After
    public void deleteUser() {
        userClient.deleteUser(accessToken);
    }
}
