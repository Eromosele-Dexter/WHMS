package views.admin;

import com.owlike.genson.Genson;
import statics.HttpMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

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
                String username = userText.getText();

                String password = new String(passwordText.getPassword());

                try {
                    URL url = new URL(LOGIN_ENDPOINT);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod(HttpMethods.POST);
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Cookie", "whms-session=yourCookieValue"); // Replace 'yourCookieValue' with the actual cookie value

                    // Using Genson to create JSON
                    Genson genson = new Genson();
                    String jsonInputString = genson.serialize(Map.of("username", username, "password", password));

                    try(OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        // Print the response
                        System.out.println("Response: " + response.toString());

                        // If login is successful, switch to the Products Management page
                        cardLayout.show(cardPanel, PRODUCT_MANAGEMENT_PAGE);
                    } else {
                        System.out.println("Login failed: " + responseCode);
                        // Additional error handling code here
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("IOException: " + ex);
                }
            }
        });

        return loginButton;
    }
}

