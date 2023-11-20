package controllers;

import apiContracts.Requests.AddProductRequest;
import apiContracts.Requests.GetProductRequest;
import apiContracts.Requests.RegisterAdminRequest;
import apiContracts.Responses.AddProductResponse;
import apiContracts.Responses.GetProductResponse;
import apiContracts.Responses.LoginAdminResponse;
import apiContracts.Responses.RegisterAdminResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import databaseConnectors.SQLiteDbConnector;
import repositories.productRepo.ProductRepository;
import services.ProductService;
import statics.Endpoints;
import statics.HttpMethods;
import statics.StatusCodes;
import utils.HttpResponse;
import utils.JsonUtils;

import java.io.IOException;

public class ProductController implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        ProductService productService = new ProductService(new ProductRepository(new SQLiteDbConnector()));

        String json = JsonUtils.readJsonFromBody(exchange);

        String httpRequestMethod = exchange.getRequestMethod();

        String requestURI = exchange.getRequestURI().getPath();

        switch(httpRequestMethod){
            case HttpMethods.POST:

                if ((Endpoints.PRODUCT_ENDPOINT+ "/create").equals(requestURI)) {

                    AddProductRequest request = (AddProductRequest) JsonUtils.mapJsonToRequest(json, new AddProductRequest());

                    AddProductResponse response = productService.handleCreateProduct(request);


                    if (response == null) {
                        new HttpResponse(exchange, "Error Creating Product. Product Type must be one of the following: " +
                                "| electronic | furniture | general |", StatusCodes.BAD_REQUEST);
                    }
                    else
                        new HttpResponse(exchange, response, StatusCodes.OK);
                }

                break;

            case HttpMethods.GET:

                if((Endpoints.PRODUCT_ENDPOINT+ "/").equals(requestURI)){

                    GetProductRequest request = (GetProductRequest) JsonUtils.mapJsonToRequest(json, new GetProductRequest());

                    GetProductResponse response = productService.handleRetrieveProduct(request);

                    if (response == null)
                        new HttpResponse(exchange, "Error retrieving product with id" + request.getId(), StatusCodes.BAD_REQUEST);
                    else
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
