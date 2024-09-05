package praktikum.jsons.generators;

import com.github.javafaker.Faker;
import praktikum.jsons.CreateUserRequestJson;
import praktikum.jsons.UserRequestJson;

public class EditUserJsonGenerator {

    Faker faker = new Faker();

    public UserRequestJson setNewEmail(CreateUserRequestJson user) {
        return new UserRequestJson(
                faker.internet().emailAddress(),
                user.getName()
        );
    }

    public UserRequestJson setNewName(CreateUserRequestJson user) {
        return new UserRequestJson(
                user.getEmail(),
                faker.name().username()
        );
    }

    public UserRequestJson setExistsEmail(CreateUserRequestJson user1, CreateUserRequestJson user2) {
        return new UserRequestJson(
                user1.getEmail(),
                user2.getName()
        );
    }
}
