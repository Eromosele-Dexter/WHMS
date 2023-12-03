package views.admin;

import com.owlike.genson.Genson;
import statics.HttpMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static statics.Endpoints.LOGIN_ENDPOINT_URL;
import static statics.SessionKeys.ADMIN_COOKIE;
import static statics.SessionKeys.WHMS_SESSION_NAME;
import static statics.ViewPages.PRODUCT_MANAGEMENT_PAGE;

public class LoginPage {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private JLabel errorLabel;

    public LoginPage(CardLayout cardLayout, JPanel cardPanel){
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        this.errorLabel = new JLabel();
        this.errorLabel.setForeground(Color.RED);
        this.errorLabel.setVisible(false);
    }

    JPanel createLoginPage() {
        JPanel loginPanel = new JPanel();

        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.PAGE_AXIS));

        loginPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username label and text field
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel userLabel = new JLabel("Username:");

        JTextField userText = new JTextField(20);

        userPanel.add(userLabel);

        userPanel.add(userText);

        // Password label and text field
        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel passwordLabel = new JLabel("Password:");

        JPasswordField passwordText = new JPasswordField(20);

        passPanel.add(passwordLabel);

        passPanel.add(passwordText);

        // Login button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton loginButton = getLoginButton(userText, passwordText);

        buttonPanel.add(loginButton);

        // Add subpanels to the main panel
        loginPanel.add(userPanel);

        loginPanel.add(passPanel);

        loginPanel.add(buttonPanel);

        // Add error label below the button
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginPanel.add(errorLabel);

        JPanel wrapperPanel = new JPanel(new GridBagLayout());

        wrapperPanel.add(loginPanel);

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
                    URL url = new URL(LOGIN_ENDPOINT_URL);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod(HttpMethods.POST);

                    conn.setDoOutput(true);

                    conn.setRequestProperty("Content-Type", "application/json");

                    conn.setRequestProperty("Cookie", WHMS_SESSION_NAME + "=" + ADMIN_COOKIE);

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
                        errorLabel.setText("Wrong admin credentials. \nServer has Stopped. \nRestart the application.");

                        errorLabel.setVisible(true);

                        System.out.println("Login failed: " + responseCode);
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

