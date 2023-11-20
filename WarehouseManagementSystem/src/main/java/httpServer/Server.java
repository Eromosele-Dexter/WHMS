package httpServer;

import com.sun.net.httpserver.HttpServer;
import controllers.AdminController;
import controllers.ProductController;
import statics.Endpoints;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Context for handling requests
        server.createContext(Endpoints.PRODUCT_ENDPOINT, new ProductController());

        server.createContext(Endpoints.ADMIN_ENDPOINT, new AdminController(server));

        server.setExecutor(null); // Use the default executor

        server.start();


    }
}
