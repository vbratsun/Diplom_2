package ru.yandex.practicum.models.orders;

import java.util.List;

public class CreateOrderRequest {
    private List<String> ingredients;

    public CreateOrderRequest(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }
}
