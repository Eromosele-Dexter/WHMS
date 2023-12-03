package views.client;

import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import statics.HttpMethods;
import statics.StatusCodes;
import statics.messageTopics;
import views.admin.ProductResponse;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static statics.Endpoints.*;
import static statics.SessionKeys.ADMIN_COOKIE;
import static statics.SessionKeys.WHMS_SESSION_NAME;


public class OrderPage {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private String cookie;

    private JTextArea orderDetailsTextArea;


    public OrderPage(CardLayout cardLayout, JPanel cardPanel, String cookie){
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.cookie = cookie;
    }



    JPanel createOrderPage() {
        JPanel orderPanel = new JPanel();

        orderPanel.setLayout(new BorderLayout());

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

//        JComboBox<String> productComboBox = new JComboBox<>(new String[]{"Product1", "Product2", "Product3", "Product4", "Product5"});

        JComboBox<String> productComboBox = new JComboBox<>(this.getProducts().toArray(new String[0]));

        selectionPanel.add(new JLabel("Step 1 Choose Product: "));

        selectionPanel.add(productComboBox);

        JComboBox<Integer> quantityComboBox = new JComboBox<>(new Integer[]{50, 100, 150, 200});

        selectionPanel.add(new JLabel("Step 2 Choose Quantity: "));

        selectionPanel.add(quantityComboBox);

        JButton chooseButton = this.getChooseButton(productComboBox, quantityComboBox);

        selectionPanel.add(chooseButton);

        orderPanel.add(selectionPanel, BorderLayout.NORTH);

        // Message board at the bottom
         this.orderDetailsTextArea = new JTextArea();

        orderDetailsTextArea.setEditable(false); // Make text area non-editable

        JScrollPane orderDetailsScrollPane = new JScrollPane(orderDetailsTextArea);

        orderDetailsScrollPane.setBorder(BorderFactory.createTitledBorder("Order Details:"));

        // Add message board to the bottom of the order panel
        orderPanel.add(orderDetailsScrollPane, BorderLayout.CENTER);

        return orderPanel;
    }

    private JButton getChooseButton(JComboBox<String> productBox, JComboBox<Integer> quantityBox){
        JButton chooseButton = new JButton("Choose");

        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedProduct = (String) productBox.getSelectedItem();

                Integer selectedQuantity = (Integer) quantityBox.getSelectedItem();


                if (selectedProduct != null && selectedQuantity != null) {
                    placeOrder(selectedProduct,selectedQuantity);
                }


            }
        });

        return chooseButton;
    }

    private List<String> getProducts() {
        List<String> products = new ArrayList<>();

        try {
            URL url = new URL(GET_PRODUCTS_ENDPOINT_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod(HttpMethods.GET);

            int status = conn.getResponseCode();

            if (status == HttpURLConnection.HTTP_OK)  {

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String inputLine;

                StringBuffer content = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                in.close();

                Genson genson = new Genson();
                ArrayList<ProductResponse> productResponse = genson.deserialize(content.toString(), new GenericType<ArrayList<ProductResponse>>() {});


                for (int i = 0; i < productResponse.size(); i++) {

                    ProductResponse product = productResponse.get(i);

                    String name = product.getProductName();

                    products.add(name);
                }
            } else {
                // Handle non-OK response
                System.out.println("Error: API request failed with status code " + status);
            }
            conn.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return products;
    }

    private void placeOrder(String productName, int quantity){
        try {
            URL url = new URL(PLACE_ORDER_ENDPOINT_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod(HttpMethods.POST);

            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", "application/json");

            conn.setRequestProperty("Cookie", cookie);

            // Using Genson to create JSON
            Genson genson = new Genson();

            String jsonInputString = genson.serialize(Map.of("productName", productName, "quantity", quantity));

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
            }
            else{
                System.out.println("Error Placing Order: " + responseCode);
            }

        }catch (IOException ex) {
            ex.printStackTrace();

            System.out.println("IOException: " + ex);
        }

    }

    public void setWebSocketClient(WebsocketClientCustomer client) {
        client.setOrdersPage(this);
    }

    public void updateMessageBoard(HashMap<String, Object> messageObject) {
        String newMessage = "New Message";

        if (messageObject.get("topic").equals(messageTopics.GENERAL)) {
            var generalResponseData = messageObject.get("data");

            if(generalResponseData instanceof String){
                newMessage = ((String) generalResponseData ) + "\n";
            }
        }

        String existingText = this.orderDetailsTextArea.getText();

        this.orderDetailsTextArea.setText(newMessage + "\n\n" + existingText);
    }
}
