package ru.yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import ru.yandex.praktikum.api.UserClient;

public class UserLoginTest
{
    private String email = "mailtestqa@yandex.ru";
    private String password = "1q2wasZx";
    private String name = "Humanoid";

    private String incorrectEmail = "mailtestqa23@yandex.ru";
    private String incorrectPassword = "sedf345gdf";

    @Before
    public void setUp()
    {
        RestAssured.baseURI = Service.BASE_URI;
        UserClient.getCreateUserResponse(new User(email, password, name));
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginUserTest()
    {
        Response loginUserResponse = UserClient.getUserLoginByEmailAndPasswordResponse(new User(email, password));

        loginUserResponse.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    public void loginIncorrectDataUserTest()
    {
        Response loginUserResponse = UserClient.getUserLoginByEmailAndPasswordResponse(new User(incorrectEmail, incorrectPassword));

        loginUserResponse.then().assertThat().statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void tearDown()
    {
        UserClient.deleteUserByLoginAndPassword(new User(email, password));
    }
}
