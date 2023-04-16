package org.example.loginTests;

import org.example.client.UserClient;
import org.example.credentials.Credentials;
import org.example.credentials.User;
import org.example.providers.UserProvider;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;


public class UserLoginTest {
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
    public void userCanLogin() throws InterruptedException {
        ValidatableResponse responseCreate = userClient.create(user);
        responseCreate.assertThat().statusCode(SC_OK);
        Thread.sleep(1000);

        ValidatableResponse responseLogin = userClient.login(Credentials.from(user)).log().all();
        int statusCode = responseLogin.extract().statusCode();
        accessToken = responseLogin.extract().path("accessToken").toString().substring(6).trim();
        Assert.assertEquals(SC_OK, statusCode);

        userClient.delete(accessToken);
    }





}