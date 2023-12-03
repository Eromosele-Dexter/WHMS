package utils;
import apiContracts.Requests.Request;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JsonUtils {

    public static String readJsonFromBody(HttpExchange exchange){
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody());

        BufferedReader br = new BufferedReader(isr);

        // Read the request body into a StringBuilder
        StringBuilder requestBody = new StringBuilder();

        String line;

        while (true) {
            try {
                if (!((line = br.readLine()) != null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            requestBody.append(line);
        }

        // Process the request body
        String jsonData = requestBody.toString();

        return jsonData;
    }

    public static Request mapJsonToRequest(String json, Request request) {
        Genson genson = new Genson();
        return genson.deserialize(json, request.getClass());
    }

    public static String convertObjectToJson(Object object){
        Genson genson = new GensonBuilder()
                .useIndentation(true)
                .create();
        return genson.serialize(object);
    }

    public static Object convertJsonStringToObject(String json, Object objectClass){
        Genson genson = new Genson();
        System.out.println("class: " + objectClass);
        return genson.deserialize(json, objectClass.getClass());
    }
}
