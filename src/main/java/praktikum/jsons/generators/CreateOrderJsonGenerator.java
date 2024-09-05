package praktikum.jsons.generators;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import praktikum.jsons.OrderRequestJson;

import java.util.ArrayList;

public class CreateOrderJsonGenerator {

    // из массива ingredients берутся от 1 до 4 рандомных ингредиентов, из них создаётся json для заказа
    public OrderRequestJson random(ArrayList<String> ingredients) {
        // размер списка ингредиентов
        int s = ingredients.size();

        // рандомная переменная для длины нового массива для конкретизации кол-ва ингредиентов в заказе
        int r = RandomUtils.nextInt(3,7);

        // новый список рандомной длины
        ArrayList<String> order = new ArrayList<>(r);

        // в новом списке каждому индексу присваиваем рандомный ингредиент
        for (int i = 0; i < r; i++) {
            order.add(i, ingredients.get(RandomUtils.nextInt(0, s-1)));
        }

        return new OrderRequestJson(order);
    }

    public OrderRequestJson empty() {
        return new OrderRequestJson();
    }

    public OrderRequestJson wrongIngredient() {
        ArrayList<String> order = new ArrayList<>(1);
        order.add(RandomStringUtils.randomAlphanumeric(24));
        return new OrderRequestJson(order);
    }
}
