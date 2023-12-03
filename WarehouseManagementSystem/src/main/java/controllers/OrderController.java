package controllers;

import apiContracts.Requests.PlaceOrderRequest;
import apiContracts.Responses.PlaceOrderResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import databaseConnectors.SQLiteDbConnector;
import models.order.Order;
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

                    PlaceOrderRequest request = (PlaceOrderRequest) JsonUtils.mapJsonToRequest(json, new PlaceOrderRequest());

                    PlaceOrderResponse response  = orderService.handlePlaceOrder(request, new Date(), cookie);

                    if(response != null) {
                        lastOrder = response.getPlacedOrder();
                    }

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
