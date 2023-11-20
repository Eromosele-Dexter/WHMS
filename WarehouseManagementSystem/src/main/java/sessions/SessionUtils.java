package sessions;

import com.sun.net.httpserver.HttpExchange;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SessionUtils {

    public static String getClientCookie(HttpExchange exchange){
        // Get the cookies from the request
        Map<String, List<String>> headers = exchange.getRequestHeaders();
        List<String> cookies = headers.get("Cookie");

        // Check if the "session" cookie exists
        String session = null;
        if (cookies != null) {
            for (String cookie : cookies) {
                if (cookie.startsWith("whms-session=")) {
                    session = cookie;
                    break;
                }
            }
        }
        return session;
    }

    public static void setClientCookie(HttpExchange exchange){
        UUID uuid = UUID.randomUUID();

        // Convert the UUID to a string
        String session = uuid.toString();

        exchange.getResponseHeaders().add("Set-Cookie", "whms-session=" + session);
    }


}
