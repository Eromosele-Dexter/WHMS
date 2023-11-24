package views.admin;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.awt.*;

public class ProductsManagementPage {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    public ProductsManagementPage(CardLayout cardLayout, JPanel cardPanel){
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
    }

    JPanel createProductsPage() {
        JPanel productsPanel = new JPanel(new BorderLayout()); // Use BorderLayout for layout

        // Dummy data for the chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(100, "Product 1", "Product 1");
        dataset.addValue(200, "Product 2", "Product 2");
        dataset.addValue(150, "Product 3", "Product 3");
        dataset.addValue(250, "Product 4", "Product 4");
        dataset.addValue(175, "Product 5", "Product 5");

        // Create the chart
        JFreeChart barChart = ChartFactory.createBarChart(
                "Warehouse Product Monitor System",
                "Products",
                "Quantity",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        // Create and add the chart panel
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(500, 300)); // Set preferred size for the chart
        productsPanel.add(chartPanel, BorderLayout.WEST); // Add the chart to the left side


        // Create the message board panel
        JTextArea messageBoard = new JTextArea();

        messageBoard.setEditable(false); // Make the text area non-editable

        messageBoard.setText("Last Order\n=============\nProduct: Product2\nQuantity: 100\nTimestamp: 2023-10-02T12:26:53.06338\n\nCurrent Product Quantity in Warehouse\n=============\nProduct 1 ==> Quantity: 100\nProduct 2 ==> Quantity: 200\nProduct 3 ==> Quantity: 150\nProduct 4 ==> Quantity: 250\nProduct 5 ==> Quantity: 175");

        messageBoard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(messageBoard); // Allow scrolling

        scrollPane.setPreferredSize(new Dimension(400, 300)); // Set preferred size for the message board

        // Add the message board panel to the right side of the products panel
        productsPanel.add(scrollPane, BorderLayout.EAST);

        return productsPanel;
    }
}
