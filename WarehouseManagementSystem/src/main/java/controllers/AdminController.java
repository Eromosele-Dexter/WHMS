package controllers;

import apiContracts.Requests.LoginAdminRequest;
import apiContracts.Requests.RegisterAdminRequest;
import apiContracts.Responses.LoginAdminResponse;
import apiContracts.Responses.RegisterAdminResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import databaseConnectors.SQLiteDbConnector;
import repositories.adminRepo.AdminRepository;
import services.AdminService;
import statics.HttpMethods;
import statics.Endpoints;
import statics.StatusCodes;
import utils.HttpResponse;
import utils.JsonUtils;

import java.io.IOException;

public class AdminController implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        AdminService adminService = new AdminService(new AdminRepository(new SQLiteDbConnector()));

        String json = JsonUtils.readJsonFromBody(exchange);

        String httpRequestMethod = exchange.getRequestMethod();

        String requestURI = exchange.getRequestURI().getPath();

        switch(httpRequestMethod){
            case HttpMethods.POST:

                if ((Endpoints.ADMIN_ENDPOINT + "/login").equals(requestURI)) {

                    LoginAdminRequest request = (LoginAdminRequest) JsonUtils.mapJsonToRequest(json, new LoginAdminRequest());

                    LoginAdminResponse response = adminService.handleAdminLogin(request);


                    if (response == null)
                        new HttpResponse(exchange, "Error Logging In Administrator.", StatusCodes.BAD_REQUEST);
                    else
                        new HttpResponse(exchange, response, StatusCodes.OK);
                }

                else if((Endpoints.ADMIN_ENDPOINT + "/register").equals(requestURI)){

                    RegisterAdminRequest request = (RegisterAdminRequest) JsonUtils.mapJsonToRequest(json, new RegisterAdminRequest());

                    RegisterAdminResponse response = adminService.handleAdminSignUp(request);

                    if (response == null)
                        new HttpResponse(exchange, "Error Signing Up Administrator.", StatusCodes.BAD_REQUEST);
                    else
                        new HttpResponse(exchange, response, StatusCodes.OK);
                }

                break;

            case HttpMethods.GET:
                break;

            case HttpMethods.DELETE:
                break;

            default:
                new HttpResponse(exchange, "Unsupported Endpoint", StatusCodes.NOT_FOUND);
                break;
        }

    }
}

