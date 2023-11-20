package models;

public class General extends Product{

    public General(String productName, double unitPrice, int currentStockQuantity, int targetMaxStockQuantity, int targetMinStockQuantity, int restockSchedule, int discountStrategyId, String productType) {
        super(productName, unitPrice, currentStockQuantity, targetMaxStockQuantity, targetMinStockQuantity, restockSchedule, discountStrategyId, productType);
    }

    /**
     * @returns product state
     */

    // TODO: use state pattern

    public String getProductState() {
        return "PRODUCT STATE TO BE IMPLEMENTED FURNITURE";
    }
}
