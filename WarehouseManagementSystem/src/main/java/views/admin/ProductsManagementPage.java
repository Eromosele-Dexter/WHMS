package views.admin;

import apiContracts.Responses.GetProductResponse;
import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import statics.HttpMethods;
import statics.messageTopics;

import javax.swing.*;
import java.awt.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static statics.Endpoints.GET_PRODUCTS_ENDPOINT_URL;

public class ProductsManagementPage {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private ArrayList<ProductResponse> productResponse;

    private DefaultCategoryDataset dataset;

    private JTextArea messageBoard;

    private JFreeChart barChart;

    private ChartPanel chartPanel;

    private OrderMessageResponse lastOrder;

    public ProductsManagementPage(CardLayout cardLayout, JPanel cardPanel){
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
    }

    public JPanel createProductsPage() {
        JPanel productsPanel = new JPanel(new BorderLayout()); // Use BorderLayout for layout

        // Data for the chart
        this.dataset = new DefaultCategoryDataset();

        this.loadAllProducts(dataset);

        // Create the chart
        this.barChart = ChartFactory.createBarChart(
                "Warehouse Product Monitor System",
                "Products",
                "Quantity",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        // Get the plot from the chart
        CategoryPlot plot = barChart.getCategoryPlot();

        // Get the renderer
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        // Decrease the item margin (space between bars within a category)
        renderer.setItemMargin(0.0);

        // Get the domain axis (CategoryAxis) and adjust the category margin (space between categories)
        CategoryAxis domainAxis = plot.getDomainAxis();

        domainAxis.setCategoryMargin(0.1);

        renderer.setMaximumBarWidth(0.25);

        // Create and add the chart panel
        this.chartPanel = new ChartPanel(barChart);

        chartPanel.setPreferredSize(new Dimension(500, 300));

        productsPanel.add(chartPanel, BorderLayout.CENTER);

        // Create the message board panel
        JTextArea messageBoard = getMessageBoard();

        JScrollPane scrollPane = new JScrollPane(messageBoard); // Allow scrolling

        scrollPane.setPreferredSize(new Dimension(400, 300));

        productsPanel.add(scrollPane, BorderLayout.EAST);

        return productsPanel;
    }

    public void setWebSocketClient(WebsocketClientAdmin client) {
        client.setProductsManagementPage(this);
    }

    public void updateMessageBoard(HashMap<String, Object> messageObject) {
        String newMessage = "New Message";

        if(messageObject.get("topic").equals(messageTopics.UPDATED_CURRENT_STOCK)) {

            var productResponseData = ((ArrayList<?>) messageObject.get("data")).toArray();

//            System.out.println("Updated Message Board Quantity1: " + m1.length);

            GetProductResponse[] products = Arrays.stream(productResponseData)
                    .filter(obj -> obj instanceof Map)
                    .map(obj -> {
                        Map<?, ?> map = (Map<?, ?>) obj;
                        GetProductResponse response = new GetProductResponse(
                                (((Map<?, Long>) obj).get("productId")).intValue(),
                                ((Map<?, String>) obj).get("productName"),
                                ((Map<?, Double>) obj).get("unitPrice"),
                                (((Map<?, Long>) obj).get("currentStockQuantity")).intValue(),
                                (((Map<?, Long>) obj).get("targetMaxStockQuantity")).intValue(),
                                (((Map<?, Long>) obj).get("targetMinStockQuantity")).intValue(),
                                (((Map<?, Long>) obj).get("restockSchedule")).intValue(),
                                (((Map<?, Long>) obj).get("discountStrategyId")).intValue(),
                                ((Map<?, String>) obj).get("productType")
                        );

                        return response;
                    })
                    .toArray(GetProductResponse[]::new);

//            System.out.println("Updated Message Board Quantity2: " + products[0].getCurrentStockQuantity());

            newMessage = this.getProductsQuantityMessage(products);

            SwingUtilities.invokeLater(() -> {
                this.loadAllProducts(dataset);
                barChart.fireChartChanged();
                chartPanel.repaint();
            });
        }
        else if(messageObject.get("topic").equals(messageTopics.LAST_ORDER)){
            var lastOrderResponseData = messageObject.get("data");

            if(lastOrderResponseData instanceof Map){
                OrderMessageResponse lastOrder = new OrderMessageResponse(
                        ((Map<?, String>) lastOrderResponseData).get("productName"),
                        (((Map<?, Long>) lastOrderResponseData).get("quantity")).intValue(),
                       new Date(((((Map<?, Long>) lastOrderResponseData).get("date")).longValue()))
                );

                newMessage = this.getLastOrderMessage(lastOrder);
            }
        } else if (messageObject.get("topic").equals(messageTopics.RESTOCK)) {
            var restockResponseData = messageObject.get("data");

            if(restockResponseData instanceof String){
                newMessage = ((String) restockResponseData) + "\n";
            }
        } else if (messageObject.get("topic").equals(messageTopics.GENERAL)) {
            var generalResponseData = messageObject.get("data");

            if(generalResponseData instanceof String){
                newMessage = ((String) generalResponseData ) + "\n";
            }
        }

        String existingText = messageBoard.getText();

        messageBoard.setText(newMessage + "\n\n" + existingText);
    }

    private JTextArea getMessageBoard() {
        this.messageBoard = new JTextArea();

        messageBoard.setEditable(false); // Make the text area non-editable

        messageBoard.setText(this.getProductsQuantityMessage());

        messageBoard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return messageBoard;
    }

    private String getProductsQuantityMessage(){
        String result = "Current Product Quantity in Warehouse\n=============\n\n";

        for(int i=0; i< this.productResponse.size();i++) {
            ProductResponse product = this.productResponse.get(i);

            result += String.format("%s ==> Quantity: %d\n\n", product.getProductName(), product.getCurrentStockQuantity());
        }
        return result;
    }

    private String getProductsQuantityMessage(GetProductResponse[] products){
        String result = "Current Product Quantity in Warehouse\n=============\n\n";

        for(int i=0; i< products.length;i++) {
            GetProductResponse product = products[i];

            result += String.format("%s ==> Quantity: %d\n\n", product.getProductName(), product.getCurrentStockQuantity());
        }
        return result;
    }
    private String getLastOrderMessage(OrderMessageResponse lastOrder){
        String result = "Last Order\n=============\n\n";

        result += String.format("Product: %s\n\nQuantity: %d\n\nTimestamp: %s\n\n\n",
                lastOrder.getProductName(), lastOrder.getCurrentStockQuantity(), this.formatDateTime(lastOrder.getDate()));

        return result;
    }


    private void loadAllProducts(DefaultCategoryDataset dataset){
        try {
            URL url = new URL(GET_PRODUCTS_ENDPOINT_URL);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod(HttpMethods.GET);

            int status = con.getResponseCode();

            if (status == HttpURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String inputLine;
                StringBuffer content = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                in.close();

                Genson genson = new Genson();
                this.productResponse = genson.deserialize(content.toString(), new GenericType<ArrayList<ProductResponse>>() {});

                ArrayList<ProductResponse>products = this.productResponse;


                for (int i = 0; i < products.size(); i++) {

                   ProductResponse product = products.get(i);

                    String name = product.getProductName();

                    int quantity = product.getCurrentStockQuantity();

                    dataset.addValue(quantity, name, name);
                }

            } else {
                // Handle non-OK response
                System.out.println("Error: API request failed with status code " + status);
            }
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private String formatDateTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return formatter.format(date);
    }


}
