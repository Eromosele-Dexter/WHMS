package views.admin;

import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import models.Product;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.json.impl.JSONArray;
import org.jfree.data.json.impl.JSONObject;
import statics.HttpMethods;
import utils.JsonUtils;

import javax.swing.*;
import java.awt.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import static statics.Endpoints.GET_PRODUCTS_ENDPOINT_URL;

public class ProductsManagementPage {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private ArrayList<ProductResponse> productResponse;

    private DefaultCategoryDataset dataset;

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
        JFreeChart barChart = ChartFactory.createBarChart(
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
        ChartPanel chartPanel = new ChartPanel(barChart);

        chartPanel.setPreferredSize(new Dimension(500, 300));

        productsPanel.add(chartPanel, BorderLayout.CENTER);

        // Create the message board panel
        JTextArea messageBoard = getMessageBoard();

        JScrollPane scrollPane = new JScrollPane(messageBoard); // Allow scrolling

        scrollPane.setPreferredSize(new Dimension(400, 300));

        productsPanel.add(scrollPane, BorderLayout.EAST);

        return productsPanel;
    }

    private JTextArea getMessageBoard() {
        JTextArea messageBoard = new JTextArea();

        messageBoard.setEditable(false); // Make the text area non-editable

        OrderMessageResponse ro = new OrderMessageResponse(); // TODO: remove line later

        ro.setDate(new Date());

        ro.setProductName("Apple TV");

        ro.setCurrentStockQuantity(30);

        messageBoard.setText(this.getLastOrderMessage(ro) + this.getProductsQuantityMessage());

        messageBoard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return messageBoard;
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

    private String getProductsQuantityMessage(){
        String result = "Current Product Quantity in Warehouse\n=============\n\n";

        for(int i=0; i< this.productResponse.size();i++) {
            ProductResponse product = this.productResponse.get(i);

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

    private String formatDateTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return formatter.format(date);
    }


}
