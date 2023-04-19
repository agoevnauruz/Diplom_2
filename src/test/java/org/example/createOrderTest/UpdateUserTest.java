package org.example.createOrderTest;

import org.example.client.UserClient;
import org.example.credentials.Credentials;
import org.example.credentials.User;
import org.example.providers.CredentialProvider;
import org.example.providers.UserProvider;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class UpdateUserTest {
    private UserClient userClient;
    private User user;
    private String accessToken;
    private ValidatableResponse responseCreate;

    @Before
    public void setUp() {
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
    public void authorizedUserCanUpdate() {
        User updatedUser = UserProvider.getRandom();
        ValidatableResponse responseLogin = userClient.login(CredentialProvider.getDefault());
        String accessToken = responseLogin.extract().path("accessToken");
        ValidatableResponse response = userClient.update(accessToken, updatedUser);
        response.statusCode(200);
    }


    @Test
    public void unauthorizedUserCannotUpdate() {
        User updatedUser = UserProvider.getRandom();
        ValidatableResponse responseUpdate = userClient.update("", updatedUser);
        assertEquals(SC_UNAUTHORIZED, responseUpdate.extract().statusCode());
    }
}
