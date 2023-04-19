package org.example.createOrderTest;

import org.example.client.OrderClient;
import org.example.client.UserClient;
import org.example.credentials.User;
import org.example.providers.UserProvider;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class GetUserOrdersTest {
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
    public void tearDown() {
        userClient.delete(accessToken);
    }

    @Test
    public void authorizedUserCanGetOrders() {
        ValidatableResponse responseOrders = orderClient.getOrders(accessToken);
        assertEquals(SC_OK, responseOrders.extract().statusCode());
    }

    @Test
    public void unauthorizedUserCannotGetOrders() {
        ValidatableResponse responseOrders = orderClient.getOrders("");
        assertEquals(SC_UNAUTHORIZED, responseOrders.extract().statusCode());
    }
}
