package praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import praktikum.jsons.generators.CreateUserJsonGenerator;
import praktikum.rests.UserRests;

@DisplayName("Создание пользователя")
public class CreateUserTests {

    private final CreateUserJsonGenerator userJson = new CreateUserJsonGenerator();
    private final UserRests userRest = new UserRests();
    private final Check check = new Check();
    private final Action action = new Action();

    private String accessToken;

    @After
    @Step("Удаление пользователя")
    public void deleteUser() {
        if (accessToken != null) {
            ValidatableResponse creationResponse = userRest.delete(accessToken);
            check.code202andSuccess(creationResponse);
            check.userRemovedMessage(creationResponse);
            accessToken = null;
        }
    }

    @Test
    @DisplayName("[+] Создание пользователя")
    public void createUser() {
        var newUser = userJson.random();  // генерация json рандомного пользователя
        ValidatableResponse createUserResponse = userRest.create(newUser);  // создание пользователя

        check.code201andSuccess(createUserResponse);  // проверка кода и сообщения создания
        accessToken = action.extractAccessToken(createUserResponse);  // сохранение токена пользователя
    }

    @Test
    @DisplayName("[-] Создание уже существующего пользователя")
    public void createExistsUser() {
        var newUser = userJson.random();
        ValidatableResponse createUserResponse = userRest.create(newUser);
        check.code201andSuccess(createUserResponse);
        accessToken = action.extractAccessToken(createUserResponse);

        ValidatableResponse userExistsResponse = userRest.create(newUser);  // попытка создания второго такого же юзера
        check.code403andSuccessFalse(userExistsResponse);
        check.userExistsMessage(userExistsResponse);
    }

    @Test
    @DisplayName("[–] Создание пользователя без email")
    public void tryCreateUserWithoutEmail() {
        var newUser = userJson.noEmail();
        ValidatableResponse createUserResponse = userRest.create(newUser);

        check.code403andSuccessFalse(createUserResponse);
        check.userNotEnoughFieldsMessage(createUserResponse);
    }

    @Test
    @DisplayName("[–] Создание пользователя без пароля")
    public void tryCreateUserWithoutPassword() {
        var newUser = userJson.noPassword();
        ValidatableResponse createUserResponse = userRest.create(newUser);

        check.code403andSuccessFalse(createUserResponse);
        check.userNotEnoughFieldsMessage(createUserResponse);
    }

    @Test
    @DisplayName("[–] Создание пользователя без имени")
    public void tryCreateUserWithoutName() {
        var newUser = userJson.noName();
        ValidatableResponse createUserResponse = userRest.create(newUser);

        check.code403andSuccessFalse(createUserResponse);
        check.userNotEnoughFieldsMessage(createUserResponse);
    }
}
