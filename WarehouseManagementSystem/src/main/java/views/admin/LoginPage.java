package views.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static statics.Endpoints.LOGIN_ENDPOINT;
import static statics.ViewPages.PRODUCT_MANAGEMENT_PAGE;

public class LoginPage {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    public LoginPage(CardLayout cardLayout, JPanel cardPanel){
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
    }

    JPanel createLoginPage() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.PAGE_AXIS));
        loginPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Components
        JLabel userLabel = new JLabel("Username:");
        JTextField userText = new JTextField();
        userText.setMaximumSize(new Dimension(400, 40)); // Set max size

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordText = new JPasswordField();
        passwordText.setMaximumSize(new Dimension(400, 40)); // Set max size

        JButton loginButton = getLoginButton(userText, passwordText);

        // Add components to panel
        loginPanel.add(userLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacer
        loginPanel.add(userText);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Spacer
        loginPanel.add(passwordLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacer
        loginPanel.add(passwordText);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer
        loginPanel.add(loginButton);


        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        wrapperPanel.add(loginPanel, gbc);

        return wrapperPanel;
    }

    private JButton getLoginButton(JTextField userText, JPasswordField passwordText) {
        JButton loginButton = new JButton("Login");
        loginButton.setMaximumSize(new Dimension(100, 40));
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.WHITE);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                String username = userText.getText();
//
//                String password = new String(passwordText.getPassword());
//
//                try {
//                    URL url = new URL(LOGIN_ENDPOINT);
//
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//                    conn.setRequestMethod("POST");
//
//                    conn.setDoOutput(true);
//
//                    String body = "username=" + username + "&password=" + password;
//
//                    OutputStream os = conn.getOutputStream();
//
//                    PrintStream ps = new PrintStream(os);
//
//                    ps.print(body);
//
//                    ps.close();
//
//                    if (conn.getResponseCode() == 200) {
//                        // If login is successful, switch to the Products Management page else server would automatically disconnect
//                        cardLayout.show(cardPanel, PRODUCT_MANAGEMENT_PAGE);
//                    }
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                    System.out.println("ex: " +ex);
//                }
                System.out.println("pressed");
                cardLayout.show(cardPanel,PRODUCT_MANAGEMENT_PAGE);
            }
        });
        return loginButton;
    }
}

