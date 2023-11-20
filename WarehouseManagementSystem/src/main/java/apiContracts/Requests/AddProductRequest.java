package apiContracts.Requests;

public class AddProductRequest extends Request{

    private String productName;
    private int currentStockQuantity;
    private double unitPrice;
    private int targetMaxStockQuantity;
    private int targetMinStockQuantity;
    private int restockSchedule;
    private int discountStrategyId;
    private String productType;


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
}
