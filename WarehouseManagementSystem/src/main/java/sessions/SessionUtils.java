package sessions;

import com.sun.net.httpserver.HttpExchange;
import org.java_websocket.handshake.ClientHandshake;

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

        System.out.println("Cookie set");
    }

    public static String getClientCookieFromHandshake(ClientHandshake handshake){
        // Retrieve the "Cookie" header

        String cookieHeader = handshake.getFieldValue("Cookie");


        if (cookieHeader != null && !cookieHeader.isEmpty()) {

            String[] cookies = cookieHeader.split("; ");

            for (String cookie : cookies) {

                String[] cookiePair = cookie.split("=", 2);

                if (cookiePair.length == 2 && "whms-session".equals(cookiePair[0].trim())) {

                    return cookiePair[1].trim(); // Return the value of the 'whms-session' cookie

                }
            }
        }

        return null;
    }


}
