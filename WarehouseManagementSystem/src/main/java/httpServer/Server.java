package httpServer;

import com.sun.net.httpserver.HttpServer;
import controllers.AdminController;
import controllers.OrderController;
import controllers.ProductController;
import statics.Endpoints;

import java.io.IOException;
import java.net.InetSocketAddress;


public class Server {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Context for handling requests

        server.createContext(Endpoints.ADMIN_ENDPOINT, new AdminController(server));

        server.createContext(Endpoints.PRODUCT_ENDPOINT, new ProductController());

        server.createContext(Endpoints.ORDER_ENDPOINT, new OrderController());

        server.setExecutor(null); // Use the default executor

        server.start();

        // Start the WebSocket server on port 8081
//        Server wsServer = new Server(8081);
//        WebSocketHandler wsHandler = new WebSocketHandler() {
//            @Override
//            public void configure(WebSocketServletFactory factory) {
//                factory.register(MyWebSocketHandler.class);
//            }
//        };
//        wsServer.setHandler(wsHandler);
//        wsServer.start();

    }
}
