package views.client;

import javax.swing.*;
import java.awt.*;

public class MainClientUI extends JFrame{
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public MainClientUI() {
        setTitle("Warehouse Management System - Client View");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Create a panel to hold different "pages" using CardLayout
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);


        add(cardPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainClientUI();
        });
    }
}
