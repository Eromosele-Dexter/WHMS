package views.admin;

import javax.swing .*;
import java.awt .*;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

import static statics.ViewPages.*;

public class MainServerUI extends JFrame{
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public MainServerUI() {
        setTitle("Warehouse Management System - Admin View");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
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

        setVisible(true);
    }
// TODO: run server and clientUI, serverUI on different threads to avoid blocking issues
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainServerUI();
        });

        final int POLL_INTERVAL = 5000; // 5 seconds

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {

            /**
             * When an object implementing interface {@code Runnable} is used
             * to create a thread, starting the thread causes the object's
             * {@code run} method to be called in that separately executing
             * thread.
             * <p>
             * The general contract of the method {@code run} is that it may
             * take any action whatsoever.
             *
             * @see Thread#run()
             */

            /**
             * The action to be performed by this timer task.
             */
            @Override
            public void run() {
                try {
                    URL url = new URL("http://localhost:8080/check-order");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");

                    int status = con.getResponseCode();
                    if (status == 200) {
                        InputStream is = con.getInputStream();
                        String lastOrder = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                        // Update the UI with this last order
                        System.out.println("Last Order: " + lastOrder);
                    }

                    con.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }, 0, POLL_INTERVAL);
    }


}


