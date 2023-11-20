package services;

import apiContracts.Requests.PlaceOrderRequest;
import apiContracts.Responses.PlaceOrderResponse;
import models.Order;
import models.Product;
import strategies.pricing.PricingStrategy;
import strategies.pricing.PricingStrategy001;
import strategies.pricing.PricingStrategy002;
import strategies.restock.IRestockOperationStrategy;
import strategies.restock.RestockByMachine;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OrderService {

    private static ProductService productService;

    private static final Queue<Order> orders = new ConcurrentLinkedQueue<>();

    private IRestockOperationStrategy restockStrategy;

    private PricingStrategy pricingStrategy;

    private List<PricingStrategy> pricingStrategies;

    public OrderService(ProductService productService) {
        this.productService = productService;
        this.pricingStrategies = new ArrayList<>();
        this.pricingStrategies.add(new PricingStrategy001());
        this.pricingStrategies.add(new PricingStrategy002());
    }


    public PlaceOrderResponse handlePlaceOrder(PlaceOrderRequest request, Date date, String cookie){
        Order placedOrder = new Order(request.getProductName(), request.getQuantity(), date, cookie);

        Product orderedProduct = this.productService.handleGetProduct(placedOrder.getProductName());

        if(placedOrder.getQuantity() > orderedProduct.getTargetMaxStockQuantity()){
            PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse(placedOrder,
                    "Order exceeds the max quantity set for this product: "+ placedOrder.getProductName() + " and cannot be processed");
            return placeOrderResponse;
        }

        orders.add(placedOrder);

        return handleProcessOrder();
    }

    public PlaceOrderResponse handleProcessOrder(){

        Order placedOrder = orders.poll();


        Product orderedProduct = this.productService.handleGetProduct(placedOrder.getProductName());

        PlaceOrderResponse placeOrderResponse = null;

        String restockingOperationInitiatedMessage = String.format("Restocking Operation for Product %s initiated",
                placedOrder.getProductName());

        String restockingOperationCompletedMessage = String.format("Restocking Operation for Product %s completed",
                placedOrder.getProductName());

        if(placedOrder.getQuantity() > orderedProduct.getCurrentStockQuantity()) {

            restockStrategy = new RestockByMachine();

            int currentQuantity = orderedProduct.getCurrentStockQuantity();

            int quantityRequested = placedOrder.getQuantity();

            orderedProduct.setCurrentStockQuantity(currentQuantity - quantityRequested);

            String orderExceedsAvailableMessage = String.format("Order for Product %s Quantity %d is  pending – order exceeds available quantity",
                    placedOrder.getProductName(), quantityRequested);

            sendMessage(orderExceedsAvailableMessage);

            sendMessage(restockingOperationInitiatedMessage);

            restockStrategy.restock(orderedProduct);

            sendMessage(restockingOperationCompletedMessage);

            placeOrderResponse =   new PlaceOrderResponse(placedOrder, "");
        }

        this.productService.handleUpdateProduct(orderedProduct, orderedProduct.getProductId());

        processOrder();

        setProductState(orderedProduct,"fulfilled");

        int randomPricingStrategyIndex = getRandomNumber(0, this.pricingStrategies.size()-1);

        pricingStrategy = pricingStrategies.get(randomPricingStrategyIndex);

        double totalPrice = pricingStrategy.priceProduct(placedOrder, orderedProduct);

        String orderFinalizedMessage = String.format("Order is finalized for Product %s and Quantity %d with total price %d",
                placedOrder.getProductName(), placedOrder.getQuantity(), totalPrice);

        sendMessage(orderFinalizedMessage);

        Product productAfterFulfilled = this.productService.handleGetProduct(placedOrder.getProductName());

        if(productAfterFulfilled.getCurrentStockQuantity() < productAfterFulfilled.getTargetMinStockQuantity()) {

            setProductState(productAfterFulfilled, "low stock");

            restockAfterFulfilled(productAfterFulfilled);
        }

        return placeOrderResponse;
    }

    private void processOrder(){
        int processingTime = getRandomNumber(30,45);

        delayFunction(processingTime);
    }
    private void delayFunction(int seconds) {
        try {
            // Delay for the specified number of seconds
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            // This is thrown if the thread is interrupted during sleep
            Thread.currentThread().interrupt();
        }
    }

    private int getRandomNumber(int min, int max) { // min and max inclusive
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    private void sendMessage(String message){

    }

    private void sendMessage(String message, String client){

    }

    private void setProductState(Product product, String state){

    }

    private void restockAfterFulfilled(Product product){
        String restockingOperationInitiatedMessage = String.format("Restocking Operation for Product %s initiated",
                product.getProductName());

        String restockingOperationCompletedMessage = String.format("Restocking Operation for Product %s completed",
                product.getProductName());

        sendMessage(restockingOperationInitiatedMessage);

        restockStrategy.restock(product);

        sendMessage(restockingOperationCompletedMessage);


    }

}
