package models.order;

import java.util.Date;

public class Order {

    private String productName;

    private int quantity;

    private String clientCookie;

    private Date date;

    public Order(String productName, int quantity, Date date, String clientCookie) {
        this.productName = productName;
        this.quantity = quantity;
        this.date = date;
        this.clientCookie = clientCookie;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getClientCookie() {
        return clientCookie;
    }

    public void setClientCookie(String clientCookie) {
        this.clientCookie = clientCookie;
    }

    @Override
    public String toString() {
        return "Product: " + productName + '\n' +
                "Quantity: " + quantity + '\n' +
                "Client Time Stamp: " + date ;
    }
}
