package apiContracts.Responses;

import utils.JsonUtils;

public class GetProductResponse {

    private int productId;
    private String productName;
    private int currentStockQuantity;
    private double unitPrice;
    private int targetMaxStockQuantity;
    private int targetMinStockQuantity;
    private int restockSchedule;
    private int discountStrategyId;

    private String productType;


    public GetProductResponse (int productId, String productName, double unitPrice, int currentStockQuantity, int targetMaxStockQuantity, int targetMinStockQuantity, int restockSchedule, int discountStrategyId, String productType) {
        this.productId = productId;
        this.productName = productName;
        this.currentStockQuantity = currentStockQuantity;
        this.unitPrice = unitPrice;
        this.targetMaxStockQuantity = targetMaxStockQuantity;
        this.targetMinStockQuantity = targetMinStockQuantity;
        this.restockSchedule = restockSchedule;
        this.discountStrategyId = discountStrategyId;
        this.productType = productType;
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
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    @Override
    public String toString() {
        return JsonUtils.convertObjectToJson(this);
    }
}
