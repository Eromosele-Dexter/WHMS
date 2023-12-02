package views.client;


import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import static statics.Endpoints.WEBSOCKET_ENDPOINT;
import static statics.ViewPages.ORDER_PAGE;

public class MainClientUI extends JFrame{
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public MainClientUI() {
        setTitle("Warehouse Management System - Client View");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Create a panel to hold different "pages" using CardLayout
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        // Create and add the order page
        OrderPage orderPage = new OrderPage(cardLayout,cardPanel);
        cardPanel.add(orderPage.createOrderPage(), ORDER_PAGE);


        add(cardPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        HashMap<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put("Cookie", "whms-session=YourSessionToken");

        WebsocketClientCustomer client = null;
        try {
            client = new WebsocketClientCustomer(new URI(WEBSOCKET_ENDPOINT), httpHeaders);
        } catch (URISyntaxException e) {
            System.out.println("Unable to establish connection to websocket server");
            throw new RuntimeException(e);

        }
        client.connect();
        SwingUtilities.invokeLater(() -> {
            new MainClientUI();
        });
    }
}
