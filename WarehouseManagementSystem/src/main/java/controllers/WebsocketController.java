package controllers;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import sessions.SessionUtils;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebsocketController extends WebSocketServer {

    private static WebsocketController instance;
    private final Map<String, WebSocket> clientConnections = new ConcurrentHashMap<>();

    private WebsocketController(InetSocketAddress address) {
        super(address);
    }

    public static WebsocketController getInstance(InetSocketAddress address) {
        if (instance == null) {
            synchronized (WebsocketController.class) {
                if (instance == null) {
                    instance = new WebsocketController(address);
                }
            }
        }
        return instance;
    }



    /**
     * @param conn
     * @param clientHandshake
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake clientHandshake) {

        System.out.println("New connection from " + conn.getRemoteSocketAddress());

        String clientId = SessionUtils.getClientCookieFromHandshake(clientHandshake);

        System.out.println("client ID: " + clientId);

        clientConnections.put(clientId, conn);
    }


    public void sendMessageToClient(String message, String clientId) {
        WebSocket conn = clientConnections.get(clientId);
        System.out.println("Client id: " + clientId);
        if (conn != null) {
            System.out.println("Sending message to admin on WS");
            conn.send(message);
        }
    }

    /**
     * @param conn
     * @param code
     * @param reason
     * @param remote
     */
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Closed connection to " + conn.getRemoteSocketAddress());
    }

    /**
     * @param conn
     * @param message
     */
    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Message from client: " + message);
    }

    /**
     * @param conn
     * @param e
     */
    @Override
    public void onError(WebSocket conn, Exception e) {
        e.printStackTrace();
    }

    /**
     *
     */
    @Override
    public void onStart() {
//        System.out.println("WebSocket server started successfully");
    }
}
