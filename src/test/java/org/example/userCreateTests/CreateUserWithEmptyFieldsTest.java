package org.example.userCreateTests;

import org.example.client.UserClient;
import org.example.credentials.User;
import org.example.providers.UserProvider;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateUserWithEmptyFieldsTest {
    private User user;
    private String message;
    private int statusCode;
    private UserClient userClient;
    private String accessToken;
    private ValidatableResponse responseCreate;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() throws InterruptedException {
        if (responseCreate.extract().statusCode() == SC_OK) {
            accessToken = responseCreate.extract().path("accessToken").toString().substring(6).trim();
            userClient.delete(accessToken);
        }
    }

    public CreateUserWithEmptyFieldsTest(User user, int statusCode, String message) {
        this.user = user;
        this.statusCode = statusCode;
        this.message = message;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {UserProvider.getWithoutEmail(), SC_FORBIDDEN, "Email, password and name are required fields"},
                {UserProvider.getWithoutPassword(), SC_FORBIDDEN, "Email, password and name are required fields"},
                {UserProvider.getWithoutName(), SC_FORBIDDEN, "Email, password and name are required fields"},
                {UserProvider.getEmpty(), SC_FORBIDDEN, "Email, password and name are required fields"},
                {UserProvider.existingUser(), SC_FORBIDDEN, "User already exists"}
        };
    }

    @Test
    public void userCreationNegativeTest() {
        responseCreate = userClient.create(user);
        int actualStatusCode = responseCreate.extract().statusCode();
        if (responseCreate.extract().jsonPath().get("message") != null) {
            String actualMessage = responseCreate.extract().path("message").toString();
            assertEquals(statusCode, actualStatusCode);
            assertEquals(message, actualMessage);
        } else {
        }
    }

}