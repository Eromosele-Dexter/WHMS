package factories.productFactories;

import models.Product;

public abstract class ProductFactory {

    public abstract Product createProduct(String productName, double unitPrice, int currentStockQuantity,int targetMaxStockQuantity, int targetMinStockQuantity, int restockSchedule, int discountStrategyId, String productType);
}
