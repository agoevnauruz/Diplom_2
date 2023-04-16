package org.example.userCreateTests;

import org.example.client.UserClient;
import org.example.credentials.User;
import org.example.providers.UserProvider;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class UserCreateTest {
    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp(){
        userClient = new UserClient();
        user = UserProvider.getRandom();
    }
    @After
    public void tearDown() throws InterruptedException {
        Thread.sleep(300);
    }
    @Test
    public void userCanBeCreated(){
        ValidatableResponse responseCreate = userClient.create(user);
        responseCreate.assertThat().statusCode(SC_OK);
        accessToken = responseCreate.extract().path("accessToken").toString().substring(6).trim();
        userClient.delete(accessToken);
    }
}