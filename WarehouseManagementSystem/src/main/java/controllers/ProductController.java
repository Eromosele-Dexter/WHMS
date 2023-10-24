package controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.JsonUtils;

import java.io.IOException;

public class ProductController implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String json = JsonUtils.readJsonFromBody(exchange);

    }
}
