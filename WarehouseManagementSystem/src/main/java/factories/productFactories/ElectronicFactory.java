package factories.productFactories;

import models.products.Electronic;
import models.products.Product;

public class ElectronicFactory extends ProductFactory{
    /**
     * @param productName
     * @param unitPrice
     * @param currentStockQuantity
     * @param targetMaxStockQuantity
     * @param targetMinStockQuantity
     * @param restockSchedule
     * @param discountStrategyId
     * @returns electronic product
     */
    @Override
    public Product createProduct(String productName, double unitPrice, int currentStockQuantity, int targetMaxStockQuantity, int targetMinStockQuantity, int restockSchedule, int discountStrategyId, String productType) {
        return new Electronic(productName, unitPrice, currentStockQuantity, targetMaxStockQuantity, targetMinStockQuantity, restockSchedule, discountStrategyId, productType);
    }
}
