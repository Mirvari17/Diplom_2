package praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.jsons.generators.CreateOrderJsonGenerator;
import praktikum.jsons.generators.CreateUserJsonGenerator;
import praktikum.rests.OrderRests;
import praktikum.rests.UserRests;

import java.util.ArrayList;

@DisplayName("Создание заказа")
public class CreateOrderTests {

    private final CreateUserJsonGenerator userJson = new CreateUserJsonGenerator();
    private final UserRests userRest = new UserRests();
    private final Check check = new Check();
    private final Action action = new Action();
    private final OrderRests orderRests = new OrderRests();
    private final CreateOrderJsonGenerator orderJson = new CreateOrderJsonGenerator();

    private String accessToken;
    private ArrayList<String> ingredients;
    private int orderNumber;

    @Before
    @DisplayName("Создание пользователя")
    public void createUser() {
        var newUser = userJson.random();  // генерация json рандомного пользователя
        ValidatableResponse createUserResponse = userRest.create(newUser);  // создание пользователя

        check.code201andSuccess(createUserResponse);  // проверка кода и сообщения создания
        accessToken = action.extractAccessToken(createUserResponse);  // сохранение токена пользователя

        ValidatableResponse createIngredientsResponse = orderRests.getIngredients();  // запрос списка ингредиентов
        check.code200andSuccess(createIngredientsResponse);
        ingredients = action.extractIngredients(createIngredientsResponse);  // сохранение списка ингредиентов

        System.out.println("\nAvailable ingredient id's:"); // вывод ингредиентов списком, для красоты
        ingredients.forEach(System.out::println);
        System.out.println("\n");
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
    @DisplayName("[+] Создание заказа")
    public void createOrder() {
        var newOrderJson = orderJson.random(ingredients);
        ValidatableResponse newOrder = orderRests.createOrder(accessToken, newOrderJson);

        check.code200andSuccess(newOrder);
        check.nameNotNull(newOrder);
        check.orderNumberNotNull(newOrder);
    }

    @Test
    @DisplayName("[+] Создание заказа без авторизации")
    public void createOrderNoAuth() {
        var newOrderJson = orderJson.random(ingredients);
        ValidatableResponse newOrder = orderRests.createOrder("", newOrderJson);

        check.code200andSuccess(newOrder);
        check.nameNotNull(newOrder);
        check.orderNumberNotNull(newOrder);
    }

    @Test
    @DisplayName("[–] Создание заказа без ингредиентов")
    public void tryCreateOrderNoIngredients() {
        var newOrderJson = orderJson.empty();
        ValidatableResponse newOrder = orderRests.createOrder(accessToken, newOrderJson);

        check.code400andSuccessFalse(newOrder);
        check.ingredientMustBeProvidedMessage(newOrder);
    }

    @Test
    @DisplayName("[–] Создание заказа с невалидным хешем ингредиента")
    public void tryCreateOrderWrongIngredients() {
        var newOrderJson = orderJson.wrongIngredient();
        ValidatableResponse newOrder = orderRests.createOrder(accessToken, newOrderJson);

        check.code500(newOrder);
    }

    @Test
    @DisplayName("[+] Получение списка заказов пользователя")
    public void getOrderList() {
        var newOrderJson = orderJson.random(ingredients);
        ValidatableResponse newOrder = orderRests.createOrder(accessToken, newOrderJson);

        check.code200andSuccess(newOrder);
        check.orderNumberNotNull(newOrder);
        orderNumber = action.saveOrderNumber(newOrder);

        ValidatableResponse orderList = orderRests.getOrdersList(accessToken);

        check.code200andSuccess(orderList);
        check.orderListIsNotNull(orderList);
        check.orderNumbersInRequestAndResponseAreTheSame(orderNumber, orderList);
    }

    @Test
    @DisplayName("[-] Получение списка заказов пользователя без авторизации")
    public void tryGetOrderListNoAuth() {
        var newOrderJson = orderJson.random(ingredients);
        ValidatableResponse newOrder = orderRests.createOrder(accessToken, newOrderJson);

        check.code200andSuccess(newOrder);
        check.orderNumberNotNull(newOrder);
        orderNumber = action.saveOrderNumber(newOrder);

        ValidatableResponse orderList = orderRests.getOrdersList("");

        check.code401andSuccessFalse(orderList);
        check.notAuthMessage(orderList);
    }
}
