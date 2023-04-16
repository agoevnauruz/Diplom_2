package org.example.client;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.credentials.Credentials;
import org.example.credentials.User;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {

    private static final String PATH = "api/auth/";
    private static final String USERS_ENDPOINT = "/api/users";
    private static final String LOGIN_ENDPOINT = "/api/login";
    private static final String DELETE_ENDPOINT = "/api/delete";

    @Step("User creation")
    public ValidatableResponse create(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .post(PATH + "register/")
                .then();
    }

    @Step("User deletion")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .spec(getSpec())
                .header("Authorization", "Bearer " + accessToken)
                .delete(DELETE_ENDPOINT)
                .then();
    }

    @Step("User login")
    public ValidatableResponse login(Credentials credentials) {
        return given()
                .spec(getSpec())
                .body(credentials)
                .post(LOGIN_ENDPOINT)
                .then();
    }

    public ValidatableResponse update(String accessToken, User user) {
        return given()
                .spec(getSpec())
                .header("Authorization", "Bearer " + accessToken)
                .body(user)
                .put(USERS_ENDPOINT)
                .then();
    }

    public RequestSpecification getSpec() {
        return given()
                .contentType(ContentType.JSON);
    }
}
