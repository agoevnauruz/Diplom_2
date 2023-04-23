package org.example.client;

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
        return sendRequestWithAuth(order, "post", accessToken);
    }

    public ValidatableResponse createWithoutAuth(Order order) {
        return sendRequestWithoutAuth(order, "post");
    }

    private ValidatableResponse sendRequestWithAuth(Order order, String method, String accessToken) {
        return given()
                .header("authorization", "bearer " + accessToken)
                .spec(getSpec())
                .body(order)
                .when()
                .request(method, PATH)
                .then();
    }

    private ValidatableResponse sendRequestWithoutAuth(Order order, String method) {
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .request(method, PATH)
                .then();
    }
}
