package usertest;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest {
    User user;
    UserClient userClient;
    ValidatableResponse response;
    private String accessToken;


    @Before
    public void setup() {
        user = User.getRandomUser();
        userClient = new UserClient();


    }

    @Test
    @DisplayName("Создание пользователя")
    public void createNewUserTest() {
        response = userClient.create(user);
        accessToken = response.extract().body().path("accessToken");
        userClient.deleteUser(accessToken);
        response.assertThat().body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Попытка создания существующего пользователя")
    public void createUserThanAlreadyExistTest() {
        accessToken = userClient.create(user).extract().body().path("accessToken");
        response = userClient.create(user);
        response.and().assertThat().body("message", equalTo("User already exists"))
                .and()
                .statusCode(SC_FORBIDDEN);
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Создание пользователя без поля email")
    public void createUserWithoutEmailFieldTest() {
        user = User.getUserWithoutEmailField();
        response = userClient.create(user);
        response.and().assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Создание пользователя без поля password")
    public void createUserWithoutPasswordFieldTest() {
        user = User.getUserWithoutPasswordField();
        response = userClient.create(user);
        response.and().assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Создание пользователя без поля name")
    public void createUserWithoutNameFieldTest() {
        user = User.getUserWithoutNameField();
        response = userClient.create(user);
        response.and().assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(SC_FORBIDDEN);
    }
}
