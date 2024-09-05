package praktikum.jsons.generators;

import com.github.javafaker.Faker;
import praktikum.jsons.CreateUserRequestJson;
import praktikum.jsons.UserRequestJson;

public class LoginUserJsonGenerator {

    Faker faker = new Faker();

    public UserRequestJson from(CreateUserRequestJson user) {
        return new UserRequestJson(
                user.getEmail(),
                user.getPassword()
        );
    }

    public UserRequestJson wrongEmail(CreateUserRequestJson user) {
        return new UserRequestJson(
                faker.internet().emailAddress(),
                user.getPassword()
        );
    }

    public UserRequestJson wrongPassword(CreateUserRequestJson user) {
        return new UserRequestJson(
                user.getEmail(),
                faker.internet().password()
        );
    }
}
