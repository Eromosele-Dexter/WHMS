package statics;

public class Endpoints {

    public static final int HTTP_PORT = 8080;
    public static final int WS_PORT = 8081;
    public static final String ADMIN_ENDPOINT = "/admin";

    public static final String PRODUCT_ENDPOINT = "/products";

    public static final String ORDER_ENDPOINT = "/orders";

    public static final String BASE_URL = "http://localhost:8080";
    public static final String LOGIN_ENDPOINT_URL = BASE_URL + ADMIN_ENDPOINT + "/login";

    public static final String GET_PRODUCTS_ENDPOINT_URL = BASE_URL + PRODUCT_ENDPOINT;

    public static final String PLACE_ORDER_ENDPOINT_URL = BASE_URL + ORDER_ENDPOINT + "/place-order";

    public static final String WEBSOCKET_ENDPOINT = "ws://127.0.0.1:"+WS_PORT;
}
