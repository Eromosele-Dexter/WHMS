package views.client;

import views.admin.LoginPage;

import javax.swing.*;
import java.awt.*;

import static statics.ViewPages.LOGIN_PAGE;
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
        SwingUtilities.invokeLater(() -> {
            new MainClientUI();
        });
    }
}
