package views.admin;

import javax.swing .*;
import java.awt .*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;


import static statics.Endpoints.WEBSOCKET_ENDPOINT;
import static statics.ViewPages.*;

public class MainServerUI extends JFrame{
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public MainServerUI() {
        setTitle("Warehouse Management System - Admin View");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Create a panel to hold different "pages" using CardLayout
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
        ProductsManagementPage productsManagementPage = new ProductsManagementPage(cardLayout, cardPanel);
        cardPanel.add(productsManagementPage.createProductsPage(), PRODUCT_MANAGEMENT_PAGE);

        add(cardPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);


    }

    public static void main(String[] args) {
        HashMap<String, String> httpHeaders = new HashMap<>();

        httpHeaders.put("Cookie", "whms-session=admin");

        WebsocketClientAdmin client = null;
        try {
            client = new WebsocketClientAdmin(new URI(WEBSOCKET_ENDPOINT), httpHeaders);
        } catch (URISyntaxException e) {
            System.out.println("Unable to establish connection to websocket server");
            throw new RuntimeException(e);

        }
        client.connect();

        SwingUtilities.invokeLater(() -> {
            new MainServerUI();
        });

    }


}


