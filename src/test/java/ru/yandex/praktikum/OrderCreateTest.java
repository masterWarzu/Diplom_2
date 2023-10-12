package ru.yandex.praktikum;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import ru.yandex.praktikum.api.OrderClient;
import ru.yandex.praktikum.api.UserClient;
import java.util.ArrayList;
import java.util.Arrays;

public class OrderCreateTest
{
    private String token;
    private String email = "mailtestqa@yandex.ru";
    private String password = "1q2wasZx";
    private String name = "Humanoid";

    private final Order order = new Order(new ArrayList<>(Arrays.asList("61c0c5a71d1f82001bdaaa6d",
                                                                        "61c0c5a71d1f82001bdaaa6f",
                                                                        "61c0c5a71d1f82001bdaa322")));

    private final Faker faker = new Faker();

    @Before
    public void setUp()
    {
        RestAssured.baseURI = Service.BASE_URI;
        Response response = UserClient.getCreateUserResponse(new User(email, password, name));
        token = response.then().extract().body().path("accessToken");
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderAfterAuthTest()
    {
        Response createUserResponse = OrderClient.getCreateOrderWithAuthResponse(token, order);

        createUserResponse.then().assertThat().statusCode(200)
                .and()
                .body("order.owner", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuthTest()
    {
        Response createUserResponse = OrderClient.getCreateOrderWithoutAuthResponse(order);

        createUserResponse.then().assertThat().statusCode(200)
                .and()
                .body("order.owner", nullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest()
    {
        Response createUserResponse = OrderClient.getCreateOrderWithAuthResponse(token, new Order());

        createUserResponse.then().assertThat().statusCode(400)
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithIncorrectHashTest()
    {
        order.addIngredient(faker.random().hex(20));

        Response createUserResponse = OrderClient.getCreateOrderWithAuthResponse(token, order);

        createUserResponse.then().assertThat().statusCode(500);
    }

    @After
    public void tearDown()
    {
        UserClient.deleteUserByToken(token);
    }
}
