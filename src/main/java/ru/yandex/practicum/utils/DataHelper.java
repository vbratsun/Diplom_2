package ru.yandex.practicum.utils;


import com.github.javafaker.Faker;
import ru.yandex.practicum.models.auth.UserInfoUpdateRequest;
import ru.yandex.practicum.models.auth.UserRegisterRequest;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DataHelper {
    private final Faker faker;

    public DataHelper() {
        this.faker = new Faker(new Locale("en"));
    }

    public UserRegisterRequest createRandomUser() {
        String email = generateEmail();
        String password = generatePassword();
        String name = email;
        return new UserRegisterRequest(email, password, name);
    }

    public UserRegisterRequest createUserWithoutPassword() {
        String email = generateEmail();
        String password = null;
        String name = email;
        return new UserRegisterRequest(email, password, name);
    }

    public UserInfoUpdateRequest createUpdatedUserData() {
        String email = generateEmail();
        String name = email;
        return new UserInfoUpdateRequest(email, name);
    }

    private String generateEmail() {
        return faker.internet().emailAddress();
    }

    private String generatePassword() {
        return faker.internet().password(8, 12, true, true, true);
    }
}
