package praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.jsons.CreateUserRequestJson;
import praktikum.jsons.generators.CreateUserJsonGenerator;
import praktikum.jsons.generators.EditUserJsonGenerator;
import praktikum.rests.UserRests;

@DisplayName("Редактирование пользователя")
public class EditUserTests {

    private final CreateUserJsonGenerator userJson = new CreateUserJsonGenerator();
    private final UserRests userRest = new UserRests();
    private final EditUserJsonGenerator editUserJson = new EditUserJsonGenerator();
    private final Check check = new Check();
    private final Action action = new Action();

    CreateUserRequestJson newUser;
    private String accessToken;
    private String accessToken2;


    @Before
    @DisplayName("[+] Создание пользователя")
    public void createUser() {
        newUser = userJson.random();  // генерация json рандомного пользователя
        ValidatableResponse createUserResponse = userRest.create(newUser);  // создание пользователя

        check.code201andSuccess(createUserResponse);  // проверка кода и сообщения создания
        accessToken = action.extractAccessToken(createUserResponse);  // сохранение токена пользователя
    }

    @After
    @Step("Удаление пользователя")
    public void deleteUser() {
        if (accessToken != null) {
            ValidatableResponse creationResponse = userRest.delete(accessToken);
            check.code202andSuccess(creationResponse);
            check.userRemovedMessage(creationResponse);
            accessToken = null;
        }
        if (accessToken2 != null) {
            ValidatableResponse creationResponse = userRest.delete(accessToken2);
            check.code202andSuccess(creationResponse);
            check.userRemovedMessage(creationResponse);
            accessToken2 = null;
        }
    }

    @Test
    @DisplayName("[+] Изменение email пользователя")
    public void editUserEmail() {
        var userNewEmail = editUserJson.setNewEmail(newUser);
        ValidatableResponse editUserResponse = userRest.edit(accessToken, userNewEmail);

        check.code201andSuccess(editUserResponse);
        check.emailOk(editUserResponse, userNewEmail);     // в ответе тот email, с которым логинились
        check.nameOk(editUserResponse, newUser);       // в ответе то имя, с которым регистрировались
    }

    @Test
    @DisplayName("[+] Изменение имени пользователя")
    public void editUserName() {
        var userNewName = editUserJson.setNewName(newUser);
        ValidatableResponse editUserResponse = userRest.edit(accessToken, userNewName);

        check.code201andSuccess(editUserResponse);
        check.emailOk(editUserResponse, userNewName);     // в ответе тот email, с которым логинились
        check.nameOk(editUserResponse, newUser);       // в ответе то имя, с которым регистрировались
    }

    @Test
    @DisplayName("[–] Попытка редактирования без авторизации")
    public void tryEditUserNoAuth() {
        var userNewEmail = editUserJson.setNewEmail(newUser);
        ValidatableResponse editUserResponse = userRest.edit("", userNewEmail);

        check.code401andSuccessFalse(editUserResponse);
        check.notAuthMessage(editUserResponse);
    }

    @Test
    @DisplayName("[–] Попытка обновить email на существующий")
    public void tryChangeEmailToExists() {
        // создание второго пользователя
        CreateUserRequestJson newUser2 = userJson.random();  // генерация json второго пользователя
        ValidatableResponse createUserResponse2 = userRest.create(newUser2);  // создание второго пользователя

        check.code201andSuccess(createUserResponse2);  // проверка кода и сообщения создания второго пользователя
        accessToken2 = action.extractAccessToken(createUserResponse2);  // сохранение токена пользователя

        // попытка смены email второму пользователю
        var userExistsEmail = editUserJson.setExistsEmail(newUser, newUser2);  // создание json для изменения второго пользователя
        ValidatableResponse editUser2Response = userRest.edit(accessToken2, userExistsEmail); // получение ответа

        check.code403andSuccessFalse(editUser2Response); // получение кода ошибки редактирования второго пользователя
        check.emailAlreadyExistsMessage(editUser2Response); // получения сообщения ошибки создания второго пользователя
    }
}
