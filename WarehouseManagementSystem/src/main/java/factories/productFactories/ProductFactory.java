package factories.productFactories;

import models.products.Product;

public abstract class ProductFactory {

    public abstract Product createProduct(String productName, double unitPrice, int currentStockQuantity,int targetMaxStockQuantity, int targetMinStockQuantity, int restockSchedule, int discountStrategyId, String productType);
}
