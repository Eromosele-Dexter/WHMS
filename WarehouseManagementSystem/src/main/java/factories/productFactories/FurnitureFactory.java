package factories.productFactories;

import models.products.Furniture;
import models.products.Product;

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
