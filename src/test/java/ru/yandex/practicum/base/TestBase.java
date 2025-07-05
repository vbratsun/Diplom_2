package ru.yandex.practicum.base;

import org.junit.After;
import org.junit.Before;
import ru.yandex.practicum.clients.Client;
import ru.yandex.practicum.models.auth.UserRegisterRequest;
import ru.yandex.practicum.models.auth.UserRegisterResponse;
import ru.yandex.practicum.utils.DataHelper;

public class TestBase {
    protected DataHelper dataHelper;
    protected Client client;
    protected UserRegisterRequest user;
    protected UserRegisterResponse registeredUser;

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
}
