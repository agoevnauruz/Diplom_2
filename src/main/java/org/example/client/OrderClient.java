package org.example.client;

import io.qameta.allure.Step;
import org.example.credentials.Order;
import org.example.providers.CredentialProvider;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    private static final String PATH = "api/orders/";
    private final UserClient userClient = new UserClient();
    private final String accessToken;

    public OrderClient() {
        ValidatableResponse responseLogin = userClient.login(CredentialProvider.getDefault());
        accessToken = responseLogin.extract().path("accessToken").toString().substring(6).trim();
    }

    public ValidatableResponse getOrders(String accessToken) {
        return given()
                .header("authorization", "bearer " + accessToken)
                .spec(getSpec())
                .when()
                .get(PATH)
                .then();
    }

    public ValidatableResponse create(String accessToken, Order order) {
        return sendRequestWithBody(order, true, "post", accessToken);
    }


    public ValidatableResponse createWithoutAuth(Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(PATH)
                .then();
    }


    private ValidatableResponse sendRequestWithBody(Order order, boolean withAuth, String method, String accessToken) {
        if (withAuth) {
            return given()
                    .header("authorization", "bearer " + accessToken)
                    .spec(getSpec())
                    .body(order)
                    .when()
                    .request(method, PATH)
                    .then();
        } else {
            return given()
                    .spec(getSpec())
                    .body(order)
                    .when()
                    .request(method, PATH)
                    .then();
        }
    }
}
