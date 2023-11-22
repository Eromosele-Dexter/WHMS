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

        loginPanel.setLayout(new GridLayout(3, 2));

        JLabel userLabel = new JLabel("Username:");

        JTextField userText = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");

        JPasswordField passwordText = new JPasswordField();

        JButton loginButton = new JButton("Login");

        loginButton.setBackground(Color.BLACK);

        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();

                String password = new String(passwordText.getPassword());

                try {
                    URL url = new URL(LOGIN_ENDPOINT);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("POST");

                    conn.setDoOutput(true);

                    String body = "username=" + username + "&password=" + password;

                    OutputStream os = conn.getOutputStream();

                    PrintStream ps = new PrintStream(os);

                    ps.print(body);

                    ps.close();

                    if (conn.getResponseCode() == 200) {
                        // If login is successful, switch to the Products Management page else server would automatically disconnect
                        cardLayout.show(cardPanel, PRODUCT_MANAGEMENT_PAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        loginPanel.add(userLabel);
        loginPanel.add(userText);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordText);
        loginPanel.add(loginButton);

        return loginPanel;
    }
}

