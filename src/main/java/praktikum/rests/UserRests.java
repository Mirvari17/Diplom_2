package praktikum.rests;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.jsons.CreateUserRequestJson;
import praktikum.jsons.UserRequestJson;

import static praktikum.Constants.*;
import static praktikum.rests.RestBase.spec;

public class UserRests {
    @Step("Отправка запроса на создание пользователя")
    public ValidatableResponse create(CreateUserRequestJson user) {
        return spec()
                .body(user)
                .when()
                .post(REGISTER_PATH)
                .then().log().all();
    }

    @Step("Отправка запроса на удаление пользователя")
    public ValidatableResponse delete(String token) {
        return spec()
                .header("Authorization", token)
                .when()
                .delete(USER_PATH)
                .then().log().all();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse login(UserRequestJson user) {
        return spec()
                .body(user)
                .when()
                .post(LOGIN_PATH)
                .then().log().all();
    }

    @Step("Изменение данных пользователя пользователя")
    public ValidatableResponse edit(String token, UserRequestJson user) {
        return spec()
                .header("Authorization", token)
                .body(user)
                .when()
                .patch(USER_PATH)
                .then().log().all();
    }
}
