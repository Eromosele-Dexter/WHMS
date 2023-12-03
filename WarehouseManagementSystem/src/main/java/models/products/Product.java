package models.products;

import productStates.RegularStockState;
import productStates.State;

public abstract class Product {
    protected int productId;
    protected String productName;
    protected int currentStockQuantity;
    protected double unitPrice;
    protected int targetMaxStockQuantity;
    protected int targetMinStockQuantity;
    protected int restockSchedule;
    protected int discountStrategyId;
    protected String productType;
    protected State state;


    public Product(String productName, double unitPrice, int currentStockQuantity, int targetMaxStockQuantity, int targetMinStockQuantity, int restockSchedule, int discountStrategyId, String productType) {
        this.productName = productName;
        this.currentStockQuantity = currentStockQuantity;
        this.unitPrice = unitPrice;
        this.targetMaxStockQuantity = targetMaxStockQuantity;
        this.targetMinStockQuantity = targetMinStockQuantity;
        this.restockSchedule = restockSchedule;
        this.discountStrategyId = discountStrategyId;
        this.productType = productType.toLowerCase();
        this.state = new RegularStockState();
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

    public String getProductType() {
        return productType.toLowerCase();
    }

    public void setProductType(String productType) {
        this.productType = productType.toLowerCase();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
