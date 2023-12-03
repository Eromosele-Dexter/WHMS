package strategies.pricing;

import models.order.Order;
import models.products.Product;

public class PricingStrategy002 extends PricingStrategy{

    private final int DISCOUNT_PERCENTAGE = 20;
    public final int REQUIRED_PURCHASE_PRICE_FOR_DISCOUNT = 1000;

    public PricingStrategy002() {
        this.id = 1;
        this.discount = DISCOUNT_PERCENTAGE;
    }
    /**
     * @param order
     * @param product
     * @return
     */
    @Override
    public double priceProduct(Order order, Product product) {
        double unitPrice = product.getUnitPrice();

        int orderQuantity = order.getQuantity();

        double totalPrice = this.calculateTotalPrice(unitPrice, orderQuantity);

        if(totalPrice >= REQUIRED_PURCHASE_PRICE_FOR_DISCOUNT ){
            totalPrice -= (totalPrice * this.discount/100);
        }

        return totalPrice;
    }
}
