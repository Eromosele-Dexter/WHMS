package views.client;


import sessions.SessionUtils;
import views.admin.ProductsManagementPage;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import static statics.Endpoints.WEBSOCKET_ENDPOINT;
import static statics.SessionKeys.WHMS_SESSION_NAME;
import static statics.ViewPages.ORDER_PAGE;

public class MainClientUI extends JFrame{
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private OrderPage orderPage;


    public MainClientUI(String number, String cookie, HashMap<String, String> httpHeaders) {
        setTitle("Warehouse Management System - Client View " + number);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(650, 410);

        setLayout(new BorderLayout());


        // Create a panel to hold different "pages" using CardLayout
        cardPanel = new JPanel();

        cardLayout = new CardLayout();

        cardPanel.setLayout(cardLayout);

        // Create and add the order page
        this.orderPage = new OrderPage(cardLayout,cardPanel, cookie);

        cardPanel.add(orderPage.createOrderPage(), ORDER_PAGE);

        initializeWebSocketClient(httpHeaders);

        add(cardPanel, BorderLayout.CENTER);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();


        int xPos = screenSize.width - getWidth();

        // Set the location of the window to the top-right corner of the screen.
        setLocation(xPos, 0);

        setVisible(true);
    }

    private void initializeWebSocketClient(HashMap<String, String> httpHeaders) {
        WebsocketClientCustomer client = null;

        try {

            client = new WebsocketClientCustomer(new URI(WEBSOCKET_ENDPOINT), httpHeaders);

            client.connect();

            orderPage.setWebSocketClient(client);

        } catch (URISyntaxException e) {

            System.out.println("Unable to establish connection to websocket server");

            throw new RuntimeException(e);

        }

    }


    public static void main(String[] args) {

        HashMap<String, String> httpHeaders = new HashMap<>();

        String cookie = WHMS_SESSION_NAME + "=" + SessionUtils.generateRandomCookie();

        httpHeaders.put("Cookie", cookie);

        SwingUtilities.invokeLater(() -> {
            new MainClientUI(args[0], cookie, httpHeaders);
        });
    }
}
