package org.example.providers;

import org.example.credentials.Credentials;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class CredentialProvider {
    @Step("Credentials of pre-defined user on server side")
    public static Credentials getDefault() {
        return new Credentials("loginDefault@domain.com", "1234");//user with given credentials already exists on server
    }
    @Step("Creds with invalid password")
    public static Credentials getWithInvalidPassword() {
        return new Credentials("loginDefault@domain.com", "");
    }

    @Step("Creds with invalid login")
    public static Credentials getWithInvalidLogin() {
        return new Credentials("", "1234");
    }

    @Step("Empty creds")
    public static Credentials getWithEmptyCreds() {
        return new Credentials();
    }
    @Step("Credentials for random user")
    public static Credentials getRandom() {
        Faker faker = new Faker();
        String login = faker.bothify("??????####@domain.com");
        String pwd = faker.regexify("[a-z1-9]{10}");
        return new Credentials(login,pwd);
    }
}
