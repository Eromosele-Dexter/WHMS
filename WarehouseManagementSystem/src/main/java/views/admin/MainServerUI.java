package views.admin;

import javax.swing .*;
import java.awt .*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;


import static statics.Endpoints.WEBSOCKET_ENDPOINT;
import static statics.SessionKeys.ADMIN_COOKIE;
import static statics.SessionKeys.WHMS_SESSION_NAME;
import static statics.ViewPages.*;

public class MainServerUI extends JFrame{
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private ProductsManagementPage productsManagementPage;

    public MainServerUI() {
        setTitle("Warehouse Management System - Admin View");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(900, 500);

        setLayout(new BorderLayout());

        cardPanel = new JPanel();

        cardLayout = new CardLayout();

        cardPanel.setLayout(cardLayout);

        // Create and add the login page
        LoginPage loginPage = new LoginPage(cardLayout,cardPanel);

        cardPanel.add(loginPage.createLoginPage(), LOGIN_PAGE);

        // Create and add the sign-up page
        SignupPage signupPage = new SignupPage(cardLayout,cardPanel);

        cardPanel.add(signupPage.createSignupPage(), SIGNUP_PAGE);

        // Create and add the products management page
        this.productsManagementPage = new ProductsManagementPage(cardLayout, cardPanel);

        cardPanel.add(productsManagementPage.createProductsPage(), PRODUCT_MANAGEMENT_PAGE);

        initializeWebSocketClient();

        add(cardPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);

        setLocation(0,0);

        setVisible(true);

    }


    private void initializeWebSocketClient() {
        HashMap<String, String> httpHeaders = new HashMap<>();

        httpHeaders.put("Cookie", WHMS_SESSION_NAME + "=" + ADMIN_COOKIE);

        WebsocketClientAdmin client = null;

        try {

            client = new WebsocketClientAdmin(new URI(WEBSOCKET_ENDPOINT), httpHeaders);

            client.connect();

            productsManagementPage.setWebSocketClient(client);

        } catch (URISyntaxException e) {

            System.out.println("Unable to establish connection to websocket server");

            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

            SwingUtilities.invokeLater(() -> {
                new MainServerUI();
            });

    }




}


