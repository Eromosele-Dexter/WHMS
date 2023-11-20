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



    @Override
    public String toString() {
        return JsonUtils.convertObjectToJson(this);
    }
}
