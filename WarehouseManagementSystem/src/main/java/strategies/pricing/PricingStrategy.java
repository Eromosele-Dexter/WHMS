package strategies.pricing;

import models.order.Order;
import models.products.Product;

public abstract class PricingStrategy {

    protected int id;

    protected int discount;

    protected int requiredQuantity;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDiscount() {
        return discount;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    protected double calculateTotalPrice(double unitPrice, int quantity){
        return unitPrice*quantity;
    }

    public abstract double priceProduct(Order order, Product product);


}
