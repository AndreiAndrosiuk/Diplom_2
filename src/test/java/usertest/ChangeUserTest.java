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

public class ChangeUserTest {
    User user;
    UserClient userClient;
    private String accessToken;
    private String data;
    Response response;

    @Before
    public void setup() {
        user = User.getRandomUser();
        userClient = new UserClient();
        accessToken = userClient.create(user).extract().path("accessToken");
    }

    @Test
    @DisplayName("Изменение имени пользователя для авторизованного юзера")
    public void changeNameForAuthorizationUserTest() {
        data = user.getName();
        user.setName(data + "s");
        response = userClient.changeUser(user, accessToken);
        user.setName(data);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Изменение логина пользователя для авторизованного юзера")
    public void changeEmailForAuthorizationUserTest() {
        data = user.getEmail();
        user.setEmail("qwe" + data);
        response = userClient.changeUser(user, accessToken);
        user.setEmail(data);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Изменение пароля пользователя для авторизованного юзера")
    public void changePasswordForAuthorizationUserTest() {
        data = user.getPassword();
        user.setPassword("we1" + data);
        response = userClient.changeUser(user, accessToken);
        user.setPassword(data);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Изменение имени пользователя для неавторизованного юзера")
    public void changeNameForNotAuthorizationUserTest() {
        data = user.getName();
        user.setName(data + "s");
        accessToken = "";
        response = userClient.changeUser(user, accessToken);
        user.setName(data);
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Изменение логина пользователя для неавторизованного юзера")
    public void changeEmailForNotAuthorizationTest() {
        data = user.getEmail();
        user.setEmail("qwe" + data);
        accessToken = "";
        response = userClient.changeUser(user, accessToken);
        user.setName(data);
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Изменение пароля пользователя для неавторизованного юзера")
    public void changePasswordForNotAuthorizationTest() {
        data = user.getPassword();
        user.setPassword("we1" + data);
        accessToken = "";
        response = userClient.changeUser(user, accessToken);
        user.setName(data);
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @After
    public void deleteUser() {
        userClient.deleteUser(accessToken);
    }

}
