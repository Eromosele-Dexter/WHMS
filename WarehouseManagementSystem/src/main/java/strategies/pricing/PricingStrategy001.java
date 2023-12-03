package strategies.pricing;

import models.order.Order;
import models.products.Product;

public class PricingStrategy001 extends PricingStrategy{

    private final int DISCOUNT_PERCENTAGE = 10;

    public final int REQUIRED_QUANTITY_FOR_DISCOUNT = 20;

    public PricingStrategy001() {
        this.id = 1;
        this.discount = DISCOUNT_PERCENTAGE;
        this.requiredQuantity = REQUIRED_QUANTITY_FOR_DISCOUNT;
    }

    /**
     * @param order
     * @return discounted price of product
     */
    @Override
    public double priceProduct(Order order, Product product) {
        double totalPrice = this.calculateTotalPrice(product.getUnitPrice(), order.getQuantity());

        if(this.requiredQuantity >= REQUIRED_QUANTITY_FOR_DISCOUNT){
            totalPrice -= (totalPrice * this.discount/100);
        }

        return totalPrice;
    }
}
