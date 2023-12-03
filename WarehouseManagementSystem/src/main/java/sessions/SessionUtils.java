package sessions;

import com.sun.net.httpserver.HttpExchange;
import org.java_websocket.handshake.ClientHandshake;
import statics.SessionKeys;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static statics.SessionKeys.WHMS_SESSION_NAME;

public class SessionUtils {

    public static String getClientCookie(HttpExchange exchange){
        // Get the cookies from the request
        Map<String, List<String>> headers = exchange.getRequestHeaders();

        List<String> cookies = headers.get("Cookie");

        // Check if the "session" cookie exists
        String session = null;

        if (cookies == null) {
            return null;
        }

        for (String cookie : cookies) {
            if (cookie.startsWith(WHMS_SESSION_NAME + "=")) {
                session = cookie.split("=", 2)[1].trim();
                break;
            }
        }

        return session;
    }

    public static String generateRandomCookie(){
        UUID uuid = UUID.randomUUID();

       return uuid.toString();
    }

    public static void setClientCookie(HttpExchange exchange) {

        String cookie = generateRandomCookie();

        exchange.getResponseHeaders().add("Set-Cookie", WHMS_SESSION_NAME + "=" + cookie);

    }

    public static String getClientCookieFromHandshake(ClientHandshake handshake){
        // Retrieve the "Cookie" header

        String cookieHeader = handshake.getFieldValue("Cookie");

        System.out.println("cookieHeader from Handshake: "+cookieHeader);

        if (cookieHeader != null && !cookieHeader.isEmpty()) {

            String[] cookies = cookieHeader.split("; ");

            for (String cookie : cookies) {

                String[] cookiePair = cookie.split("=", 2);

                if (cookiePair.length == 2 && WHMS_SESSION_NAME.equals(cookiePair[0].trim())) {

                    return cookiePair[1].trim();

                }
            }
        }

        return null;
    }


}
