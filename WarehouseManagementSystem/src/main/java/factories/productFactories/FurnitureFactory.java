package factories.productFactories;

import models.Furniture;
import models.Product;

public class FurnitureFactory extends ProductFactory{

    /**
     * @param productName
     * @param unitPrice
     * @param currentStockQuantity
     * @param targetMaxStockQuantity
     * @param targetMinStockQuantity
     * @param restockSchedule
     * @param discountStrategyId
     * @returns Product
     */

    @Override
    public Product createProduct(String productName, double unitPrice, int currentStockQuantity, int targetMaxStockQuantity, int targetMinStockQuantity, int restockSchedule, int discountStrategyId, String productType) {
        return new Furniture(productName, unitPrice, currentStockQuantity, targetMaxStockQuantity, targetMinStockQuantity,restockSchedule,discountStrategyId, productType);
    }
}
