package factories.productFactories;

import models.products.General;
import models.products.Product;

public class GeneralFactory extends ProductFactory{
    /**
     * @param productName
     * @param unitPrice
     * @param currentStockQuantity
     * @param targetMaxStockQuantity
     * @param targetMinStockQuantity
     * @param restockSchedule
     * @param discountStrategyId
     * @returns general product
     */
    @Override
    public Product createProduct(String productName, double unitPrice, int currentStockQuantity, int targetMaxStockQuantity, int targetMinStockQuantity, int restockSchedule, int discountStrategyId, String productType) {
        return new General(productName, unitPrice, currentStockQuantity, targetMaxStockQuantity, targetMinStockQuantity, restockSchedule, discountStrategyId, productType);
    }
}
