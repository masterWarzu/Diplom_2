package ru.yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import ru.yandex.praktikum.api.UserClient;

public class UserChangeTest
{
    private String token;
    private String email = "mailtestqa@yandex.ru";
    private String password = "1q2wasZx";
    private String name = "Humanoid";

    private String newEmail = "mailtestqa23@yandex.ru";
    private String newPassword = "Zx1a2sq";
    private String newName= "Alien";

    @Before
    public void setUp()
    {
        RestAssured.baseURI = Service.BASE_URI;
        Response response = UserClient.getCreateUserResponse(new User(email, password, name));

        token = response.then().extract().body().path("accessToken");
    }

    @Test
    @DisplayName("Изменение пользователя с авторизацией")
    public void changeUserAfterAuthTest()
    {
        Response createUserResponse = UserClient.getUserChangeWithAuthResponse(token, new User(newEmail, newPassword, newName));

        createUserResponse.then().assertThat().statusCode(200)
                .and()
                .body("user.email", equalTo(newEmail))
                .and()
                .body("user.name", equalTo(newName));
    }

    @Test
    @DisplayName("Изменение пользователя без авторизации")
    public void changeUserWithoutAuthTest()
    {
        Response createUserResponse = UserClient.getUserChangeWithoutAuthResponse(new User(newEmail, newPassword, newName));

        createUserResponse.then().assertThat().statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown()
    {
        UserClient.deleteUserByToken(token);
    }
}
