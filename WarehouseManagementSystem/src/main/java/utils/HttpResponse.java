package utils;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class HttpResponse {

    public HttpResponse(HttpExchange exchange, Object responseObject, int statusCode){
        try {
            HttpResponse(exchange,responseObject,statusCode);
        } catch (IOException e) {
            System.out.println("Error returning HTTP response");
            throw new RuntimeException(e);
        }
    }

    public void HttpResponse(HttpExchange exchange, Object responseObject, int statusCode) throws IOException {
        String response = JsonUtils.convertObjectToJson(responseObject);
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
