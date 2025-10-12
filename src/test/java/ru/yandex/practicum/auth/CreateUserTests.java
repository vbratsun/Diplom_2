package ru.yandex.practicum.auth;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;
import ru.yandex.practicum.base.TestBase;
import ru.yandex.practicum.constants.ErrorMessages;
import ru.yandex.practicum.models.auth.UserRegisterResponse;
import ru.yandex.practicum.models.errors.ErrorResponse;

import static org.junit.Assert.*;

public class CreateUserTests extends TestBase {
    @Test
    @DisplayName("Проверка создания уникального пользователя")
    @Description("Позитивная проверка возможности создания уникального пользователя")
    public void userCanBeCreatedSuccessfullyTest() {
        Response registerUserResponse = this.authClient.registerUser(this.user);
        assertEquals("Неверный статус-код", HttpStatus.SC_OK, registerUserResponse.statusCode());

        this.registeredUser = registerUserResponse.as(UserRegisterResponse.class);
        assertTrue("Неверное значение поля 'success'", this.registeredUser.isSuccess());
        assertEquals("Неверное значение поля 'name'", this.user.getName(), this.registeredUser.getUser().getName());
    }

    @Test
    @DisplayName("Проверка невозможности создания 2х одинаковых пользователей")
    @Description("Проверка невозможности создания 2х пользователей с одинаковыми данными")
    public void unableToCreateTwoSameUsersTest() {
        Response registerUserResponse = this.authClient.registerUser(this.user);
        registerUserResponse.then().statusCode(HttpStatus.SC_OK);

        this.registeredUser = registerUserResponse.as(UserRegisterResponse.class);

        Response sameRegisterUserResponse = this.authClient.registerUser(this.user);
        assertEquals("Неверный статус-код", HttpStatus.SC_FORBIDDEN, sameRegisterUserResponse.statusCode());

        ErrorResponse errorResponse = sameRegisterUserResponse.as(ErrorResponse.class);
        assertFalse("Неверное значение поля 'success'", errorResponse.isSuccess());
        assertEquals("Неверное значение поля 'message'", ErrorMessages.CREATE_USER_ALREADY_EXISTS, errorResponse.getMessage());
    }

    @Test
    @DisplayName("Проверка невозможности создания пользователя без пароля")
    @Description("Проверка невозможности создания пользователя без обязательного поля пароль")
    public void unableToCreateUserWithoutPasswordTest() {
        Response registerUserResponse = this.authClient.registerUser(this.dataHelper.createUserWithoutPassword());
        assertEquals("Неверный статус-код", HttpStatus.SC_FORBIDDEN, registerUserResponse.statusCode());

        ErrorResponse errorResponse = registerUserResponse.as(ErrorResponse.class);
        assertFalse("Неверное значение поля 'success'", errorResponse.isSuccess());
        assertEquals("Неверное значение поля 'message'", ErrorMessages.CREATE_USER_NOT_ENOUGH_DATA, errorResponse.getMessage());
    }
}
