package org.example.providers;

import org.example.credentials.Order;
import io.qameta.allure.Step;

import java.util.List;

public class OrderProvider {
    @Step("Default order")
    public static Order getDefault() {
        return new Order(List.of("61c0c5a71d1f82001bdaaa73", "61c0c5a71d1f82001bdaaa75"));
    }

    @Step("Empty order")
    public static Order getEmpty() {
        return new Order();
    }

    @Step("Order with invalid hash")
    public static Order getWithInvalidHash() {
        return new Order(List.of("123"));
    }
}