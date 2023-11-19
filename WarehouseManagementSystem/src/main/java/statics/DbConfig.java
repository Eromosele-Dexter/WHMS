package statics;

public class DbConfig {

    private static final String BASE_DB_PATH = "src/main/resources/database";
    public static final String ADMIN_DB_CONNECTION_STRING = "jdbc:sqlite:" + BASE_DB_PATH + "/WHS-admin.db";
    public static final String PRODUCTS_DB_CONNECTION_STRING = "jdbc:sqlite:" + BASE_DB_PATH + "/WHS-products.db";

}
