package ru.yandex.practicum.base;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import ru.yandex.practicum.models.auth.UserLoginRequest;
import ru.yandex.practicum.models.auth.UserRegisterResponse;

public class LoginUserTestBase extends TestBase{
    protected Response registerUserResponse;
    protected UserLoginRequest userLogin;


    @Override
    @Before
    public void setUp() {
        super.setUp();
        this.registerUserResponse = this.authClient.registerUser(this.user);
        this.registerUserResponse.then().statusCode(HttpStatus.SC_OK);
        this.registeredUser = registerUserResponse.as(UserRegisterResponse.class);
        this.userLogin = new UserLoginRequest(this.user.getEmail(), this.user.getPassword());
    }
}
