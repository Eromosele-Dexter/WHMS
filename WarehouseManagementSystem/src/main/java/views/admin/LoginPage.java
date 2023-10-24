package views.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    public LoginPage(CardLayout cardLayout, JPanel cardPanel){
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
    }

    JPanel createLoginPage() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));


        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Authentication logic
                // If login is successful, switch to the Products page
                cardLayout.show(cardPanel, "Products");
            }
        });


        return loginPanel;
    }
}
