package ru.yandex.practicum.clients;

import io.restassured.response.Response;
import ru.yandex.practicum.models.auth.UserLoginRequest;
import ru.yandex.practicum.models.auth.UserRegisterRequest;

import static io.restassured.RestAssured.given;

public class Client {

    protected static final String QA_STELLARBURGERS_SERVICE = "https://stellarburgers.nomoreparties.site";

    private static final String API_AUTH_REGISTER = "/api/auth/register";
    private static final String API_AUTH_LOGIN = "/api/auth/login";
    private static final String API_AUTH_LOGOUT = "/api/auth/logout";
    private static final String API_AUTH_TOKEN = "/api/auth/token";
    private static final String API_AUTH_USER = "/api/auth/user";

    private static final String API_INGREDIENTS = "/api/ingredients";
    private static final String API_ORDERS = "/api/orders";
    private static final String API_ORDERS_ALL = "/api/orders/all";
    private static final String API_PASSWORD_RESET = "/api/password-reset";

    public Response registerUser(UserRegisterRequest userRegisterRequest) {
        return given()
                .baseUri(QA_STELLARBURGERS_SERVICE)
                .header("Content-type", "application/json")
                .body(userRegisterRequest)
                .when()
                .post(API_AUTH_REGISTER);
    }

    public Response deleteUser(String accessToken) {
        return given()
                .baseUri(QA_STELLARBURGERS_SERVICE)
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .delete(API_AUTH_USER);
    }

    public Response loginUser(UserLoginRequest userLoginRequest) {
        return given()
                .baseUri(QA_STELLARBURGERS_SERVICE)
                .header("Content-type", "application/json")
                .body(userLoginRequest)
                .when()
                .post(API_AUTH_LOGIN);
    }

    public Response getIngredients() {
        return given()
                .baseUri(QA_STELLARBURGERS_SERVICE)
                .header("Content-type", "application/json")
                .when()
                .get(API_INGREDIENTS);
    }
}
