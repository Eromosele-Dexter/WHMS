package controllers;

import apiContracts.Requests.LoginAdminRequest;
import apiContracts.Requests.RegisterAdminRequest;
import apiContracts.Responses.LoginAdminResponse;
import apiContracts.Responses.RegisterAdminResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import databaseConnectors.SQLiteDbConnector;
import repositories.adminRepo.AdminRepository;
import services.AdminService;
import sessions.SessionUtils;
import statics.HttpMethods;
import statics.Endpoints;
import statics.StatusCodes;
import utils.HttpResponse;
import utils.JsonUtils;
import views.admin.MainServerUI;

import java.io.IOException;

public class AdminController implements HttpHandler {

    HttpServer server;
    public AdminController(HttpServer server){
        this.server = server;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        AdminService adminService = new AdminService(new AdminRepository(SQLiteDbConnector.getInstance()));

        String json = JsonUtils.readJsonFromBody(exchange);

        String httpRequestMethod = exchange.getRequestMethod();

        String requestURI = exchange.getRequestURI().getPath();

        switch(httpRequestMethod){
            case HttpMethods.POST:

                if ((Endpoints.ADMIN_ENDPOINT + "/login").equals(requestURI)) {

                    LoginAdminRequest request = (LoginAdminRequest) JsonUtils.mapJsonToRequest(json, new LoginAdminRequest());

                    LoginAdminResponse response = adminService.handleAdminLogin(request);

                    String cookie = SessionUtils.getClientCookie(exchange);

                    System.out.println("Admin Cookie: " + cookie);

                    if (response == null) {
                        new HttpResponse(exchange, "Error Logging In Administrator.", StatusCodes.BAD_REQUEST);
                        server.stop(3);
                    }
                    else {
                        new HttpResponse(exchange, response, StatusCodes.OK);
                        // Start client 1
                        new Thread(() -> views.client.MainClientUI.main(new String[]{"1"})).start();

                        // Start client 2
                        new Thread(() -> views.client.MainClientUI.main(new String[]{"2"})).start();
                    }
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

