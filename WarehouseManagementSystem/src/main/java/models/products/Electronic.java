package models.products;

public class Electronic extends Product {

    public Electronic(String productName, double unitPrice, int currentStockQuantity, int targetMaxStockQuantity, int targetMinStockQuantity, int restockSchedule, int discountStrategyId, String productType) {
        super(productName, unitPrice, currentStockQuantity, targetMaxStockQuantity, targetMinStockQuantity, restockSchedule, discountStrategyId, productType);
    }

    /**
     * @returns product state
     */

    public String getProductState() {
        return this.getProductState();
    }
}
