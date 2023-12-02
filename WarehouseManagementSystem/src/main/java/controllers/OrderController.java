package controllers;

import apiContracts.Requests.AddProductRequest;
import apiContracts.Requests.GetProductRequest;
import apiContracts.Requests.PlaceOrderRequest;
import apiContracts.Responses.AddProductResponse;
import apiContracts.Responses.GetProductResponse;
import apiContracts.Responses.PlaceOrderResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import databaseConnectors.SQLiteDbConnector;
import models.Order;
import repositories.productRepo.ProductRepository;
import services.OrderService;
import services.ProductService;
import sessions.SessionUtils;
import statics.Endpoints;
import statics.HttpMethods;
import statics.StatusCodes;
import utils.HttpResponse;
import utils.JsonUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OrderController implements HttpHandler {



    private static Order lastOrder = null;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        OrderService orderService = new OrderService(new ProductService(new ProductRepository(SQLiteDbConnector.getInstance())));

        String json = JsonUtils.readJsonFromBody(exchange);

        String httpRequestMethod = exchange.getRequestMethod();

        String requestURI = exchange.getRequestURI().getPath();

        switch(httpRequestMethod){
            case HttpMethods.POST:

                if ((Endpoints.ORDER_ENDPOINT+ "/place-order").equals(requestURI)) {

                    String cookie = SessionUtils.getClientCookie(exchange);

                    System.out.println("Cookie: " + cookie);

                    if(cookie == null) {
                        SessionUtils.setClientCookie(exchange);
                        cookie = SessionUtils.getClientCookie(exchange);
                    }


                    PlaceOrderRequest request = (PlaceOrderRequest) JsonUtils.mapJsonToRequest(json, new PlaceOrderRequest());

                    PlaceOrderResponse response  = orderService.handlePlaceOrder(request, new Date(), cookie);

                    if(response != null) {
                        lastOrder = response.getPlacedOrder();
                    }

//TODO:                    Order is received for Product X and Quantity Y”

                    if (response == null) {
                        new HttpResponse(exchange, "Error Placing Order for " + request.getProductName() + " with quantity: " + request.getQuantity(), StatusCodes.BAD_REQUEST);
                    }
                    else
                        new HttpResponse(exchange, response.getOrderResponse(), StatusCodes.OK);
                }

                break;

            case HttpMethods.GET:
                if((Endpoints.ORDER_ENDPOINT+ "/check-order").equals(requestURI)){


                    Order response = lastOrder != null ? lastOrder : null;

                    new HttpResponse(exchange, response, StatusCodes.OK);
                }
                break;

            case HttpMethods.DELETE:
                break;

            default:
                new HttpResponse(exchange, "Unsupported Endpoint", StatusCodes.NOT_FOUND);
                break;
        }
    }
}
