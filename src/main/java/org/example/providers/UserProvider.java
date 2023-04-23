package org.example.providers;

import org.example.credentials.User;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class UserProvider {
    @Step("User without email")
    public static User getWithoutEmail() {
        return new User("", "1234", "Max");
    }

    @Step("User without password")
    public static User getWithoutPassword() {
        return new User("max@gmail.com", "", "Max");
    }

    @Step("User without name")
    public static User getWithoutName() {
        return new User("max@gmail.com", "1234", "");
    }

    @Step("Without credentials")
    public static User getEmpty() {
        return new User();
    }
    @Step("Random user")
    public static User getRandom() {
        Faker faker = new Faker();
        return new User(CredentialProvider.getRandom().getEmail(), CredentialProvider.getRandom().getPassword(), faker.name().firstName());
    }

    public static Object existingUser() {
        String existingUserEmail = "max@gmail.com";
        String existingUserPassword = "1234";
        String existingUserName = "Max";
        return new User(existingUserEmail, existingUserPassword, existingUserName);
    }
}