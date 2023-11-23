package httpServer;

import com.sun.net.httpserver.HttpServer;
import controllers.AdminController;
import controllers.OrderController;
import controllers.ProductController;
import controllers.WebsocketController;
import statics.Endpoints;

import java.io.IOException;
import java.net.InetSocketAddress;

import static statics.Endpoints.HTTP_PORT;
import static statics.Endpoints.WS_PORT;


public class Server {
    public static void main(String[] args) throws IOException {

        try {
            // Start the HTTP server on port 8080

            HttpServer server = HttpServer.create(new InetSocketAddress(HTTP_PORT), 0);

            // Context for handling requests

            server.createContext(Endpoints.ADMIN_ENDPOINT, new AdminController(server));

            server.createContext(Endpoints.PRODUCT_ENDPOINT, new ProductController());

            server.createContext(Endpoints.ORDER_ENDPOINT, new OrderController());

            server.setExecutor(null);

            server.start();

            System.out.println("HTTP Server started on port " + HTTP_PORT);


            // Start the WebSocket on port 8081

            InetSocketAddress address = new InetSocketAddress(WS_PORT);

            WebsocketController wsController = WebsocketController.getInstance(address);

            new Thread(wsController).start(); // Start the WebSocket server in a new thread

            System.out.println("WebSocket Server started on port " + WS_PORT);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                server.stop(0); // Stop the HTTP server
                try {
                    wsController.stop(); // Stop the WebSocket server
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Servers have been stopped.");
            }));
    } catch (IOException e) {
            e.printStackTrace();

            System.err.println("Server failed to start: " + e.getMessage());
        }

    }
}
