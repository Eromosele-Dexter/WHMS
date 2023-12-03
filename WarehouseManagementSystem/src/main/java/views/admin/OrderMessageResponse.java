package views.admin;

import java.util.Date;

public class OrderMessageResponse {
    private String productName;
    private int currentStockQuantity;

    private Date date;

    public OrderMessageResponse(){}
    public OrderMessageResponse(String productName, int currentStockQuantity, Date date) {
        this.productName = productName;
        this.currentStockQuantity = currentStockQuantity;
        this.date = date;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCurrentStockQuantity() {
        return currentStockQuantity;
    }

    public void setCurrentStockQuantity(int currentStockQuantity) {
        this.currentStockQuantity = currentStockQuantity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
