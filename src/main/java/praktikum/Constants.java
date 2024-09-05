package praktikum;

public class Constants {
    // API Paths
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    public static final String REGISTER_PATH = "/api/auth/register";
    public static final String USER_PATH = "/api/auth/user";
    public static final String LOGIN_PATH = "/api/auth/login";
    public static final String INGR_PATH = "/api/ingredients";
    public static final String ORDERS_PATH = "/api/orders";

    // Response messages
    public static final String USER_DELETED = "User successfully removed";
    public static final String USER_EXISTS = "User already exists";
    public static final String USER_NOT_ENOUGH_FIELDS = "Email, password and name are required fields";
    public static final String CREDS_INCORRECT = "email or password are incorrect";
    public static final String NO_AUTH = "You should be authorised";
    public static final String USER_EMAIL_EXISTS = "User with such email already exists";
    public static final String NO_INGREDIENTS = "Ingredient ids must be provided";
}
