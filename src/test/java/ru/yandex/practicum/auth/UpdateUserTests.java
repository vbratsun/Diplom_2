package ru.yandex.practicum.auth;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.base.LoginUserTestBase;
import ru.yandex.practicum.constants.ErrorMessages;
import ru.yandex.practicum.models.auth.UserInfoResponse;
import ru.yandex.practicum.models.auth.UserInfoUpdateRequest;
import ru.yandex.practicum.models.errors.ErrorResponse;

import static org.junit.Assert.*;

public class UpdateUserTests extends LoginUserTestBase {

    protected UserInfoUpdateRequest updatedUserInfo;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        this.updatedUserInfo = this.dataHelper.createUpdatedUserData();
    }

    @Test
    @DisplayName("Проверка обновления информации пользователя")
    @Description("Позитивная проверка возможности обновить данные пользователя")
    public void userInfoCanBeUpdatedTest(){
        Response updatedUserInfoResponse = this.authClient.updateUser(this.registeredUser.getAccessToken(), this.updatedUserInfo);
        assertEquals("Неверный статус-код", HttpStatus.SC_OK, updatedUserInfoResponse.statusCode());

        UserInfoResponse userInfoResponse = updatedUserInfoResponse.as(UserInfoResponse.class);
        assertTrue("Неверное значение поля 'success'", userInfoResponse.isSuccess());
        assertEquals("Неверное значение поля 'email'", this.updatedUserInfo.getEmail(), userInfoResponse.getUser().getEmail());
        assertEquals("Неверное значение поля 'name'", this.updatedUserInfo.getName(), userInfoResponse.getUser().getName());
    }

    @Test
    @DisplayName("Проверка невозможности обновления информации пользователя")
    @Description("Проверка невозможности обновить данные пользователя без авторизации")
    public void unableToUpdateUserInfoWithoutAuthorizationTest(){
        Response updatedUserInfoResponse = this.authClient.updateUser("", this.updatedUserInfo);
        assertEquals("Неверный статус-код", HttpStatus.SC_UNAUTHORIZED, updatedUserInfoResponse.statusCode());

        ErrorResponse errorResponse = updatedUserInfoResponse.as(ErrorResponse.class);
        assertFalse("Неверное значение поля 'success'", errorResponse.isSuccess());
        assertEquals("Неверное значение поля 'message'", ErrorMessages.UPDATE_USER_NOT_AUTHORIZED, errorResponse.getMessage());
    }
}
