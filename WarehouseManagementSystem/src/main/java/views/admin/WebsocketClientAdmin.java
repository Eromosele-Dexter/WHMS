package views.admin;

import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import models.messages.GeneralOrderMessage;
import models.messages.Message;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;

import javax.swing.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class WebsocketClientAdmin extends WebSocketClient {

    private ProductsManagementPage productsManagementPage;

    public WebsocketClientAdmin(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    public void setProductsManagementPage(ProductsManagementPage page) {
        this.productsManagementPage = page;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
//        System.out.println("Opened admin connection: " + handshakedata.toString());
    }

    @Override
    public void onMessage(String message) {
//        System.out.println("Received message ADMIN: " + message);

        Genson genson = new Genson();
        var response = genson.deserialize(message, new GenericType<>(){});

        SwingUtilities.invokeLater(() -> {
            if (productsManagementPage != null) {
                productsManagementPage.updateMessageBoard(((HashMap<String, Object>)response));
            }
        });

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Closed with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("An error occurred:" + ex.getMessage());
    }



    @Override
    public void onWebsocketHandshakeReceivedAsClient(WebSocket conn, ClientHandshake request, ServerHandshake response) throws InvalidDataException {
        super.onWebsocketHandshakeReceivedAsClient(conn, request, response);
    }



}
