package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import praktikum.jsons.CreateUserRequestJson;
import praktikum.jsons.UserRequestJson;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static praktikum.Constants.*;

public class Check {

    @Step("Код ответа: 200 OK")
    public void code201andSuccess(ValidatableResponse response) {
        response.assertThat().statusCode(HTTP_OK)
                .body("success", is(true));
    }

    @Step("Код ответа: 200 OK")
    public void code200andSuccess(ValidatableResponse response) {
        response.assertThat().statusCode(HTTP_OK)
                .body("success", is(true));
    }

    @Step("Код ответа: 202 ACCEPTED")
    public void code202andSuccess(ValidatableResponse response) {
        response.assertThat().statusCode(HTTP_ACCEPTED)
                .body("success", is(true));
    }

    @Step("Код ответа: 400 BAD REQUEST")
    public void code400andSuccessFalse(ValidatableResponse response) {
        response.assertThat().statusCode(HTTP_BAD_REQUEST)
                .body("success", is(false));
    }

    @Step("Код ответа: 500 INTERNAL SERVER ERROR")
    public void code500(ValidatableResponse response) {
        response.assertThat().statusCode(HTTP_INTERNAL_ERROR);
    }

    @Step("Текст ответа: пользователь успешно удалён")
    public void userRemovedMessage(ValidatableResponse response) {
        response.body("message", is(USER_DELETED));
    }

    @Step("Код ответа: 403 FORBIDDEN")
    public void code403andSuccessFalse(ValidatableResponse response) {
        response.assertThat().statusCode(HTTP_FORBIDDEN)
                .body("success", is(false));
    }

    @Step("Текст ответа: пользователь уже существует")
    public void userExistsMessage(ValidatableResponse response) {
        response.body("message", is(USER_EXISTS));
    }

    @Step("Текст ответа: не все обязательные поля заполнены")
    public void userNotEnoughFieldsMessage(ValidatableResponse response) {
        response.body("message", is(USER_NOT_ENOUGH_FIELDS));
    }

    @Step("Email при авторизации корректный")
    public void emailOk(ValidatableResponse response, UserRequestJson requestEmail) {
        response.body("user.email", is(requestEmail.getEmail()));
    }

    @Step("Имя при авторизации корректно")
    public void nameOk(ValidatableResponse response, CreateUserRequestJson requestName) {
        response.body("user.name", is(requestName.getName()));
    }

    @Step("Код ответа: 401 UNAUTHORIZED")
    public void code401andSuccessFalse(ValidatableResponse response) {
        response.assertThat().statusCode(HTTP_UNAUTHORIZED)
                .body("success", is(false));
    }

    @Step("Текст ответа: Некорректный email или пароль")
    public void credsIncorrectMessage(ValidatableResponse response) {
        response.body("message", is(CREDS_INCORRECT));
    }

    @Step("Текст ответа: вы должны быть авторизованы")
    public void notAuthMessage(ValidatableResponse response) {
        response.body("message", is(NO_AUTH));
    }

    @Step("Текст ответа: пользователь с таким email уже существует")
    public void emailAlreadyExistsMessage(ValidatableResponse response) {
        response.body("message", is(USER_EMAIL_EXISTS));
    }

    @Step("Название не пустое")
    public void nameNotNull(ValidatableResponse response) {
        response.body("name", notNullValue());
    }

    @Step("Номер заказа не пустой")
    public void orderNumberNotNull(ValidatableResponse response) {
        response.body("order.number", notNullValue());
    }

    @Step("Список заказов не пустой")
    public void orderListIsNotNull(ValidatableResponse response) {
        response.body("orders", notNullValue());
    }

    @Step("Текст ответа: должен быть передан ингредиент")
    public void ingredientMustBeProvidedMessage(ValidatableResponse response) {
        response.body("message", is(NO_INGREDIENTS));
    }

    @Step("Номер заказа в ответе такой же, как и в запросе")
    public void orderNumbersInRequestAndResponseAreTheSame(int orderNumber, ValidatableResponse response) {
        int o = response.extract().path("orders.number[0]");
        Assert.assertEquals(o, orderNumber);
    }
}