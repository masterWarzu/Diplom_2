package ru.yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import ru.yandex.praktikum.api.OrderClient;
import ru.yandex.praktikum.api.UserClient;

public class OrderGetTest
{
    private String token;
    private String email = "mailtestqa@yandex.ru";
    private String password = "1q2wasZx";
    private String name = "Humanoid";

    @Before
    public void setUp()
    {
        RestAssured.baseURI = Service.BASE_URI;
        Response response = UserClient.getCreateUserResponse(new User(email, password, name));

        token = response.then().extract().body().path("accessToken");
    }

    @Test
    @DisplayName("Получение списка заказов пользователя после авторизации")
    public void getIngredientsWithAuthTest()
    {
        Response response = OrderClient.getIngredientsWithAuthResponse(token);

        response.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение списка заказов пользователя без авторизации")
    public void getIngredientsWithoutAuthTest()
    {
        Response response = OrderClient.getIngredientsWithoutAuthResponse();

        response.then().assertThat().statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown()
    {
        UserClient.deleteUserByToken(token);
    }
}
