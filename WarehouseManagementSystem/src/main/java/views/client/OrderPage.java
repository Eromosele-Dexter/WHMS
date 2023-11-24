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
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public OrderPage(CardLayout cardLayout, JPanel cardPanel){
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
    }


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

        orderPanel.setLayout(new BorderLayout());

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JComboBox<String> productComboBox = new JComboBox<>(new String[]{"Product1", "Product2", "Product3", "Product4", "Product5"}); // Add your products here

        selectionPanel.add(new JLabel("Step1 Choose Product: "));

        selectionPanel.add(productComboBox);

        JComboBox<Integer> quantityComboBox = new JComboBox<>(new Integer[]{50, 100, 150, 200});

        selectionPanel.add(new JLabel("Step2 Choose Quantity: "));

        selectionPanel.add(quantityComboBox);

        JButton chooseButton = new JButton("Choose");

        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic to handle the order
            }
        });
        selectionPanel.add(chooseButton);

        orderPanel.add(selectionPanel, BorderLayout.NORTH);

        // Message board at the bottom
        JTextArea orderDetailsTextArea = new JTextArea();
        orderDetailsTextArea.setEditable(false); // Make text area non-editable
        JScrollPane orderDetailsScrollPane = new JScrollPane(orderDetailsTextArea);
        orderDetailsScrollPane.setBorder(BorderFactory.createTitledBorder("Order Details:"));

        // Add message board to the bottom of the order panel
        orderPanel.add(orderDetailsScrollPane, BorderLayout.CENTER);

        return orderPanel;
    }
}
