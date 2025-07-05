package ru.yandex.practicum.orders;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.base.LoginUserTestBase;
import ru.yandex.practicum.clients.OrderClient;
import ru.yandex.practicum.constants.ErrorMessages;
import ru.yandex.practicum.models.errors.ErrorResponse;
import ru.yandex.practicum.models.orders.OrderCreateRequest;
import ru.yandex.practicum.models.orders.IngredientsResponse;
import ru.yandex.practicum.models.orders.OrderCreateResponse;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CreateOrderTests extends LoginUserTestBase {

    private OrderClient orderClient;
    private IngredientsResponse ingredients;
    private OrderCreateRequest order;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        this.orderClient = new OrderClient();

        Response ingredientsResponse = this.orderClient.getIngredients();
        ingredientsResponse.then().statusCode(HttpStatus.SC_OK);
        this.ingredients = ingredientsResponse.as(IngredientsResponse.class);
        this.order = this.dataHelper.createOrder(this.ingredients.getData());
    }

    @Test
    @DisplayName("Проверка невозможности создания пустого заказа с авторизацией")
    @Description("Проверка невозможности создать пустой заказ авторизованным пользователем")
    public void unableToCreateEmptyOrderWithAuthorizationTest(){
        OrderCreateRequest emptyOrder = new OrderCreateRequest(new ArrayList<>());
        Response createOrderResponse = this.orderClient.createOrder(this.registeredUser.getAccessToken(), emptyOrder);
        assertEquals("Неверный статус-код", HttpStatus.SC_BAD_REQUEST, createOrderResponse.statusCode());

        ErrorResponse errorResponse = createOrderResponse.as(ErrorResponse.class);
        assertFalse("Неверное значение поля 'success'", errorResponse.isSuccess());
        assertEquals("Неверное значение поля 'message'", ErrorMessages.CREATE_ORDER_WITHOUT_ID, errorResponse.getMessage());
    }

    @Test
    @DisplayName("Проверка невозможности создания пустого заказа без авторизацией")
    @Description("Проверка невозможности создать пустой заказ неавторизованным пользователем")
    public void unableToCreateEmptyOrderWithoutAuthorizationTest(){
        OrderCreateRequest emptyOrder = new OrderCreateRequest(new ArrayList<>());
        Response createOrderResponse = this.orderClient.createOrder("", emptyOrder);
        assertEquals("Неверный статус-код", HttpStatus.SC_BAD_REQUEST, createOrderResponse.statusCode());

        ErrorResponse errorResponse = createOrderResponse.as(ErrorResponse.class);
        assertFalse("Неверное значение поля 'success'", errorResponse.isSuccess());
        assertEquals("Неверное значение поля 'message'", ErrorMessages.CREATE_ORDER_WITHOUT_ID, errorResponse.getMessage());
    }

    @Test
    @DisplayName("Проверка возможности создания заказа")
    @Description("Проверка возможности создать заказ авторизованным пользователем")
    public void createOrderWithIngredientsWithAuthorizationTest(){
        Response createOrderResponse = this.orderClient.createOrder(this.registeredUser.getAccessToken(), this.order);
        assertEquals("Неверный статус-код", HttpStatus.SC_OK, createOrderResponse.statusCode());

        OrderCreateResponse orderResponse = createOrderResponse.as(OrderCreateResponse.class);
        assertTrue("Неверное значение поля 'success'", orderResponse.isSuccess());
        assertEquals("Неверное количество ингредиентов", this.ingredients.getData().size(), orderResponse.getOrder().getIngredients().size());
    }

    @Test
    @DisplayName("Проверка невозможности создания заказа")
    @Description("Проверка невозможности создать заказ неавторизованным пользователем")
    public void unableToCreateOrderWithIngredientsWithoutAuthorizationTest(){
        Response createOrderResponse = this.orderClient.createOrder("", this.order);
        assertEquals("Неверный статус-код", HttpStatus.SC_BAD_REQUEST, createOrderResponse.statusCode());

        ErrorResponse errorResponse = createOrderResponse.as(ErrorResponse.class);
        assertFalse("Неверное значение поля 'success'", errorResponse.isSuccess());
        assertEquals("Неверное значение поля 'message'", ErrorMessages.CREATE_ORDER_WITHOUT_ID, errorResponse.getMessage());
    }

    @Test
    @DisplayName("Проверка невозможности создания заказа с невалидными хешами")
    @Description("Проверка невозможности создания заказа с невалидными хешами ингредиентов")
    public void unableToCreateOrderWithWrongIngredientsHashWithAuthorizationTest(){
        OrderCreateRequest orderWithWrongHash = new OrderCreateRequest(Arrays.asList("111111111111111111111111", "222222222222222222222222"));
        Response createOrderResponse = this.orderClient.createOrder(this.registeredUser.getAccessToken(), orderWithWrongHash);
        assertEquals("Неверный статус-код", HttpStatus.SC_BAD_REQUEST, createOrderResponse.statusCode());

        ErrorResponse errorResponse = createOrderResponse.as(ErrorResponse.class);
        assertFalse("Неверное значение поля 'success'", errorResponse.isSuccess());
        assertEquals("Неверное значение поля 'message'", ErrorMessages.CREATE_ORDER_WRONG_HASH, errorResponse.getMessage());
    }

}
