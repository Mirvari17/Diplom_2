package praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.jsons.CreateUserRequestJson;
import praktikum.jsons.generators.CreateUserJsonGenerator;
import praktikum.jsons.generators.LoginUserJsonGenerator;
import praktikum.rests.UserRests;

@DisplayName("Авторизация пользователя")
public class LoginUserTests {

    private final CreateUserJsonGenerator userJson = new CreateUserJsonGenerator();
    private final UserRests userRest = new UserRests();
    private final LoginUserJsonGenerator loginJson = new LoginUserJsonGenerator();
    private final Check check = new Check();
    private final Action action = new Action();

    CreateUserRequestJson newUser;
    private String accessToken;

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
    }

    @Test
    @DisplayName("[+] Логин пользователя")
    public void loginUser() {
        var newLogin = loginJson.from(newUser);
        ValidatableResponse loginUserResponse = userRest.login(newLogin);

        check.code201andSuccess(loginUserResponse);
        check.emailOk(loginUserResponse, newLogin);     // в ответе тот email, с которым логинились
        check.nameOk(loginUserResponse, newUser);       // в ответе то имя, с которым регистрировались
    }

    @Test
    @DisplayName("[–] Логин c неверным логином")
    public void tryLoginUserWrongEmail() {
        var newLoginWrongEmail = loginJson.wrongEmail(newUser);
        ValidatableResponse loginUserResponse = userRest.login(newLoginWrongEmail);

        check.code401andSuccessFalse(loginUserResponse);
        check.credsIncorrectMessage(loginUserResponse);
    }

    @Test
    @DisplayName("[–] Логин с неверным паролем")
    public void tryLoginUserWrongPassword() {
        var newLoginWrongPassword = loginJson.wrongPassword(newUser);
        ValidatableResponse loginUserResponse = userRest.login(newLoginWrongPassword);

        check.code401andSuccessFalse(loginUserResponse);
        check.credsIncorrectMessage(loginUserResponse);
    }
}
