package strategies.pricing;

import models.Order;
import models.Product;

public class PricingStrategy002 extends PricingStrategy{

    public final int REQUIRED_PURCHASE_PRICE_FOR_DISCOUNT = 1000;

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

        if(unitPrice >= REQUIRED_PURCHASE_PRICE_FOR_DISCOUNT ){
            totalPrice -= (totalPrice * this.discount/100);
        }

        return totalPrice;
    }
}
