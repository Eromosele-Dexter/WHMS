package views.admin;

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
        JPanel productsPanel = new JPanel();
        //TODO: Create components for the products page (e.g., list of products)


        return productsPanel;
    }
}
