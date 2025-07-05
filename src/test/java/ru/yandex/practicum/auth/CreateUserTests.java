package ru.yandex.practicum.auth;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.clients.Client;
import ru.yandex.practicum.constants.ErrorMessages;
import ru.yandex.practicum.models.auth.UserRegisterRequest;
import ru.yandex.practicum.models.auth.UserRegisterResponse;
import ru.yandex.practicum.models.errors.ErrorResponse;
import ru.yandex.practicum.utils.DataHelper;

import static org.junit.Assert.*;

public class CreateUserTests {

    private DataHelper dataHelper;
    private Client client;
    private UserRegisterRequest user;
    private UserRegisterResponse registeredUser;

    @Before
    public void setUp() {
        this.dataHelper = new DataHelper();
        this.client = new Client();
        this.user = this.dataHelper.createRandomUser();
    }

    @After
    public void tearDown() {
        try {
            String token = this.registeredUser.getAccessToken(); //TODO: возможно надо обработать в условии
            this.client.deleteUser(token);
        } catch (Exception e) {
            System.out.println("Не удалось удалить пользователя: " + e);
        }
    }

    @Test
    @DisplayName("Проверка создания уникального пользователя")
    @Description("Позитивная проверка возможности создания уникального пользователя")
    public void userCanBeCreatedSuccessfullyTest() {
        Response registerUserResponse = this.client.registerUser(this.user);
        assertEquals("Неверный статус-код", HttpStatus.SC_OK, registerUserResponse.statusCode());

        this.registeredUser = registerUserResponse.as(UserRegisterResponse.class);
        assertTrue("Неверное значение поля 'success'", this.registeredUser.isSuccess());
        assertEquals("Неверное значение поля 'name'", this.user.getName(), this.registeredUser.getUser().getName());
    }

    @Test
    @DisplayName("Проверка невозможности создания 2х одинаковых пользователей")
    @Description("Проверка невозможности создания 2х пользователей с одинаковыми данными")
    public void unableToCreateTwoSameUsersTest() {
        Response registerUserResponse = this.client.registerUser(this.user);
        registerUserResponse.then().statusCode(HttpStatus.SC_OK);

        this.registeredUser = registerUserResponse.as(UserRegisterResponse.class);

        Response sameRegisterUserResponse = this.client.registerUser(this.user);
        assertEquals("Неверный статус-код", HttpStatus.SC_FORBIDDEN, sameRegisterUserResponse.statusCode());

        ErrorResponse errorResponse = sameRegisterUserResponse.as(ErrorResponse.class);
        assertFalse("Неверное значение поля 'success'", errorResponse.isSuccess());
        assertEquals("Неверное значение поля 'message'", ErrorMessages.CREATE_USER_ALREADY_EXISTS, errorResponse.getMessage());
    }

    @Test
    @DisplayName("Проверка невозможности создания пользователя без пароля")
    @Description("Проверка невозможности создания пользователя без обязательного поля пароль")
    public void unableToCreateUserWithoutPasswordTest() {
        Response registerUserResponse = this.client.registerUser(this.dataHelper.createUserWithoutPassword());
        assertEquals("Неверный статус-код", HttpStatus.SC_FORBIDDEN, registerUserResponse.statusCode());

        ErrorResponse errorResponse = registerUserResponse.as(ErrorResponse.class);
        assertFalse("Неверное значение поля 'success'", errorResponse.isSuccess());
        assertEquals("Неверное значение поля 'message'", ErrorMessages.CREATE_USER_NOT_ENOUGH_DATA, errorResponse.getMessage());
    }
}
