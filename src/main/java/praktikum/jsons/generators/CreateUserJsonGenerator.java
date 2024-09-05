package praktikum.jsons.generators;

import com.github.javafaker.Faker;
import praktikum.jsons.CreateUserRequestJson;

public class CreateUserJsonGenerator {

    Faker faker = new Faker();

    public CreateUserRequestJson random() {
        return new CreateUserRequestJson(
                faker.internet().emailAddress(),
                faker.internet().password(),
                faker.name().username()
        );
    }

    public CreateUserRequestJson noEmail() {
        return new CreateUserRequestJson(
                null,
                faker.internet().password(),
                faker.name().username()
        );
    }

    public CreateUserRequestJson noPassword() {
        return new CreateUserRequestJson(
                faker.internet().emailAddress(),
                null,
                faker.name().username()
        );
    }

    public CreateUserRequestJson noName() {
        return new CreateUserRequestJson(
                faker.internet().emailAddress(),
                faker.internet().password(),
                null
        );
    }
}
