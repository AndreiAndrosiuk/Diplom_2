package usertest;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class AuthorizationUserTest {
    User user;
    UserClient userClient;
    private String accessToken;
    private String email;
    private String password;
    Response response;

    @Before
    public void setup() {
        user = User.getRandomUser();
        userClient = new UserClient();
        accessToken = userClient.create(user).extract().path("accessToken");
    }

    @Test
    @DisplayName("Авторизация существующим пользователем")
    public void successfulAuthorizationTest() {
        userClient.login(user, accessToken)
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Авторизация с некорректным логином")
    public void incorrectLoginAuthorizationTest() {
        email = user.getEmail();
        user.setEmail("q" + email);
        response = userClient.login(user, accessToken);
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Авторизация с некорректным паролем")
    public void incorrectPasswordAuthorizationTest() {
        password = user.getPassword();
        user.setPassword(password + "5");
        response = userClient.login(user, accessToken);
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @After
    public void deleteUser() {
        userClient.deleteUser(accessToken);
    }
}
