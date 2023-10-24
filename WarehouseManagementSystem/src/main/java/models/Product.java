package models;

public class Product {
    private int productId;
    private String productName;
    private int currentStockQuantity;
    private double unitPrice;
    private int targetMaxStockQuantity;
    private int targetMinStockQuantity;
    private int restockSchedule;
    private int discountStrategyId;

    public Product(int productId, String productName, double unitPrice, int targetMaxStockQuantity, int targetMinStockQuantity, int restockSchedule, int discountStrategyId) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.targetMaxStockQuantity = targetMaxStockQuantity;
        this.targetMinStockQuantity = targetMinStockQuantity;
        this.restockSchedule = restockSchedule;
        this.discountStrategyId = discountStrategyId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCurrentStockQuantity() {
        return currentStockQuantity;
    }

    public void setCurrentStockQuantity(int currentStockQuantity) {
        this.currentStockQuantity = currentStockQuantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getTargetMaxStockQuantity() {
        return targetMaxStockQuantity;
    }

    public void setTargetMaxStockQuantity(int targetMaxStockQuantity) {
        this.targetMaxStockQuantity = targetMaxStockQuantity;
    }

    public int getTargetMinStockQuantity() {
        return targetMinStockQuantity;
    }

    public void setTargetMinStockQuantity(int targetMinStockQuantity) {
        this.targetMinStockQuantity = targetMinStockQuantity;
    }

    public int getRestockSchedule() {
        return restockSchedule;
    }

    public void setRestockSchedule(int restockSchedule) {
        this.restockSchedule = restockSchedule;
    }

    public int getDiscountStrategyId() {
        return discountStrategyId;
    }

    public void setDiscountStrategyId(int discountStrategyId) {
        this.discountStrategyId = discountStrategyId;
    }
}
