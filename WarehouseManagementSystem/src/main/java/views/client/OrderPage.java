package views.client;

import statics.HttpMethods;
import statics.StatusCodes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static statics.Endpoints.GET_PRODUCTS_ENDPOINT;
import static statics.Endpoints.PLACE_ORDER_ENDPOINT;

public class OrderPage {

    private List<String> getProducts() {
        List<String> products = new ArrayList<>();

        try {
            URL url = new URL(GET_PRODUCTS_ENDPOINT);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod(HttpMethods.GET);

            if (conn.getResponseCode() == StatusCodes.OK) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;

                while ((line = reader.readLine()) != null) {
                    products.add(line);
                }

                reader.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return products;
    }

    JPanel createOrderPage() {
        JPanel orderPanel = new JPanel();

        orderPanel.setLayout(new GridLayout(3, 2));

        List<String> products = getProducts();

        JComboBox<String> productDropdown = new JComboBox<>(products.toArray(new String[0]));

        Integer[] quantities = {1, 5, 10, 50, 100, 250, 300, 350, 400, 500, 1000};

        JComboBox<Integer> quantityDropdown = new JComboBox<>(quantities);

        JButton orderButton = new JButton("Choose");

        JLabel orderStatus = new JLabel();

        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String product = (String) productDropdown.getSelectedItem();

                Integer quantity = (Integer) quantityDropdown.getSelectedItem();
                try {
                    URL url = new URL(PLACE_ORDER_ENDPOINT);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod(HttpMethods.POST);

                    conn.setDoOutput(true);

                    String body = "product=" + product + "&quantity=" + quantity;

                    OutputStream os = conn.getOutputStream();

                    PrintStream ps = new PrintStream(os);

                    ps.print(body);

                    ps.close();

                    if (conn.getResponseCode() == 200) {

                        orderStatus.setText("Order received");

                    } else {

                        orderStatus.setText("Order failed");

                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        orderPanel.add(new JLabel("Product:"));

        orderPanel.add(productDropdown);

        orderPanel.add(new JLabel("Quantity:"));

        orderPanel.add(quantityDropdown);

        orderPanel.add(orderButton);

        orderPanel.add(orderStatus);

        return orderPanel;
    }
}
