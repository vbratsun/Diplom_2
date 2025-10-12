package ru.yandex.practicum.auth;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;
import ru.yandex.practicum.base.LoginUserTestBase;
import ru.yandex.practicum.constants.ErrorMessages;
import ru.yandex.practicum.models.auth.UserLoginRequest;
import ru.yandex.practicum.models.auth.UserLoginResponse;
import ru.yandex.practicum.models.errors.ErrorResponse;

import static org.junit.Assert.*;

public class LoginUserTests extends LoginUserTestBase {

    @Test
    @DisplayName("Проверка логина пользователя")
    @Description("Позитивная проверка возможности пользователя залогиниться")
    public void  userCanLoginSuccessfullyTest(){
        Response userLoginResponse = this.authClient.loginUser(this.userLogin);
        assertEquals("Неверный статус-код", HttpStatus.SC_OK, userLoginResponse.statusCode());

        UserLoginResponse loggedInUser = userLoginResponse.as(UserLoginResponse.class);
        assertTrue("Неверное значение поля 'success'", loggedInUser.isSuccess());
        assertEquals("Неверное значение поля 'name'", this.user.getName(), loggedInUser.getUser().getName());
    }

    @Test
    @DisplayName("Проверка невозможности залогиниться с неверными данными")
    @Description("Проверка невозможности залогиниться с неверным логином и паролем")
    public void  unableToLoginWithWrongEmailAndPasswordTest(){
        UserLoginRequest userWithWrongCredentials = new UserLoginRequest(this.user.getEmail()+"asd", this.user.getPassword()+"asd");
        Response userWithWrongCredentialsResponse = this.authClient.loginUser(userWithWrongCredentials);
        assertEquals("Неверный статус-код", HttpStatus.SC_UNAUTHORIZED, userWithWrongCredentialsResponse.statusCode());

        ErrorResponse errorResponse = userWithWrongCredentialsResponse.as(ErrorResponse.class);
        assertFalse("Неверное значение поля 'success'", errorResponse.isSuccess());
        assertEquals("Неверное значение поля 'message'", ErrorMessages.LOGIN_USER_INCORRECT_CREDENTIALS, errorResponse.getMessage());
    }
}
