package org.example.createOrderTest;

import org.example.client.OrderClient;
import org.example.client.UserClient;
import org.example.credentials.Order;
import org.example.credentials.User;
import org.example.providers.CredentialProvider;
import org.example.providers.OrderProvider;
import org.example.providers.UserProvider;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;

public class CreateOrderTest {
    private OrderClient orderClient;
    private UserClient userClient;
    private User user;
    private String accessToken;
    private ValidatableResponse responseCreate;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        userClient = new UserClient();
        user = UserProvider.getRandom();
        responseCreate = userClient.create(user);
        accessToken = responseCreate.extract().path("accessToken").toString().substring(6).trim();
    }

    @After
    public void tearDown() throws InterruptedException {
        userClient.delete(accessToken);
        Thread.sleep(300);
    }

    @Test
    public void authorizedUserCanCreateOrderWithIngredients() {
        Order order = OrderProvider.getDefault();
        ValidatableResponse responseOrder = orderClient.create(accessToken, order);
        assertEquals(SC_OK, responseOrder.extract().statusCode());
    }

    @Test
    public void unauthorizedUserCannotCreateOrder() {
        Order order = OrderProvider.getDefault();
        ValidatableResponse responseOrder = orderClient.createWithoutAuth(order);
        assertEquals(SC_UNAUTHORIZED, responseOrder.extract().statusCode());
    }



    @Test
    public void authorizedUserCanCreateOrderWithoutIngredients() {
        Order order = OrderProvider.getEmpty();
        ValidatableResponse responseOrder = orderClient.create(accessToken, order);
        assertEquals(SC_BAD_REQUEST, responseOrder.extract().statusCode());
    }

    @Test
    public void authorizedUserCannotCreateOrderWithInvalidHash() {
        Order order = OrderProvider.getWithInvalidHash();
        ValidatableResponse responseOrder = orderClient.create(accessToken, order);
        assertEquals(SC_INTERNAL_SERVER_ERROR, responseOrder.extract().statusCode());
    }

    @Test
    public void deleteUserTest() {
        ValidatableResponse responseLogin = userClient.login(CredentialProvider.getDefault());
        String accessToken = responseLogin.extract().path("accessToken");
        ValidatableResponse response = userClient.delete(accessToken);
        response.statusCode(200);
    }

}

