package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.ArrayList;

public class Action {
    @Step("Сохранение токена авторизации")
    public String extractAccessToken(ValidatableResponse response) {
        return response.extract().path("accessToken");
    }

    @Step("Сохранение списка ингредиентов")
    public ArrayList<String> extractIngredients(ValidatableResponse response) {
        return response.extract().path("data._id");
    }

    @Step("Сохранение номера заказа")
    public int saveOrderNumber(ValidatableResponse response) {
        return response.extract().path("order.number");
    }
}
