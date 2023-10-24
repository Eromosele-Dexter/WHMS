package statics;

public class DbQueries {
    public static final String ADMIN_TABLE = "admin";
    public static final String COUNT_ADMIN_QUERY = "SELECT COUNT(*) FROM " + ADMIN_TABLE;

    public static final String ADD_ADMIN_QUERY = "INSERT INTO " + ADMIN_TABLE + " (username, password) VALUES (?, ?)";
}
