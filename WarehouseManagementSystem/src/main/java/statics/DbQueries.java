package statics;

public class DbQueries {
    public static final String ADMIN_TABLE = "admin";

    public static final String PRODUCTS_TABLE = "products";
    public static final String COUNT_ADMIN_QUERY = "SELECT COUNT(*) FROM " + ADMIN_TABLE;

    public static final String ADD_ADMIN_QUERY = "INSERT INTO " + ADMIN_TABLE + " (username, password) VALUES (?, ?)";

    public static final String ADD_PRODUCT_QUERY = "INSERT INTO " + PRODUCTS_TABLE +
            " (name, " +
            "current_stock_quantity, " +
            "unit_price, " +
            "target_max_quantity, " +
            "target_min_quantity, " +
            "restock_schedule, " +
            "discount_strategy_id, " +
            "product_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String UPDATE_PRODUCT_QUERY = "UPDATE " + PRODUCTS_TABLE +
            " SET name = ?, " +
            "current_stock_quantity = ?, " +
            "unit_price = ?, " +
            "target_max_quantity = ?, " +
            "target_min_quantity = ?, " +
            "restock_schedule = ?, " +
            "discount_strategy_id = ? " +
            "WHERE id = ?";



}
