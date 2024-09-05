package praktikum.rests;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.jsons.OrderRequestJson;

import static praktikum.Constants.INGR_PATH;
import static praktikum.Constants.ORDERS_PATH;
import static praktikum.rests.RestBase.spec;

public class OrderRests {

    @Step("Получение списка ингредиентов")
    public ValidatableResponse getIngredients() {
        return spec()
                .when()
                .get(INGR_PATH)
                .then().log().all();
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrder(String token, OrderRequestJson order) {
        return spec()
                .header("Authorization", token)
                .body(order)
                .post(ORDERS_PATH)
                .then().log().all();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrdersList(String token) {
        return spec()
                .header("Authorization", token)
                .get(ORDERS_PATH)
                .then().log().all();
    }
}
