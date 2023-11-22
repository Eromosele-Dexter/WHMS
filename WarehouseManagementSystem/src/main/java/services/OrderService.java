package services;

import apiContracts.Requests.PlaceOrderRequest;
import apiContracts.Responses.PlaceOrderResponse;
import models.Order;
import models.Product;
import productStates.LowStockState;
import productStates.RegularStockState;
import productStates.RestockingToFulfillOrderState;
import strategies.pricing.PricingStrategy;
import strategies.pricing.PricingStrategy001;
import strategies.pricing.PricingStrategy002;
import strategies.restock.IRestockOperationStrategy;
import strategies.restock.RestockByLabour;
import strategies.restock.RestockByMachine;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OrderService {

    private static ProductService productService;

    private static final Queue<Order> orders = new ConcurrentLinkedQueue<>();

    private IRestockOperationStrategy restockStrategy;

    private PricingStrategy pricingStrategy;

    private List<PricingStrategy> pricingStrategies;

    private List<IRestockOperationStrategy> restockOperationStrategies;

    public OrderService(ProductService productService) {
        this.productService = productService;
        this.pricingStrategies = new ArrayList<>();
        this.restockOperationStrategies = new ArrayList<>();
        this.pricingStrategies.add(new PricingStrategy001());
        this.pricingStrategies.add(new PricingStrategy002());
        this.restockOperationStrategies.add(new RestockByMachine());
        this.restockOperationStrategies.add(new RestockByLabour());
    }


    public PlaceOrderResponse handlePlaceOrder(PlaceOrderRequest request, Date date, String cookie){
        Order placedOrder = new Order(request.getProductName(), request.getQuantity(), date, cookie);

        Product orderedProduct = this.productService.handleGetProduct(placedOrder.getProductName());

        if(placedOrder.getQuantity() > orderedProduct.getTargetMaxStockQuantity()){

            PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse(placedOrder,"");

            sendMessage("Order exceeds the max quantity set for this product: "+ placedOrder.getProductName() + " and cannot be processed");

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

        int randomRestockStrategyIndex = getRandomNumber(0, this.restockOperationStrategies.size()-1);

        restockStrategy = this.restockOperationStrategies.get(randomRestockStrategyIndex);

        if(placedOrder.getQuantity() > orderedProduct.getCurrentStockQuantity()) {

            int currentQuantity = orderedProduct.getCurrentStockQuantity();

            int quantityRequested = placedOrder.getQuantity();


            String orderExceedsAvailableMessage = String.format("Order for Product %s Quantity %d is  pending – order exceeds available quantity",
                    placedOrder.getProductName(), quantityRequested);

            sendMessage(orderExceedsAvailableMessage);

            sendMessage(restockingOperationInitiatedMessage);

            restockStrategy.restock(this.productService, orderedProduct);

            sendMessage(restockingOperationCompletedMessage);

            orderedProduct.setCurrentStockQuantity(currentQuantity - quantityRequested);

            placeOrderResponse =   new PlaceOrderResponse(placedOrder, "");
        }

        this.productService.handleUpdateProduct(orderedProduct, orderedProduct.getProductId());

        processOrder();

        int randomPricingStrategyIndex = getRandomNumber(0, this.pricingStrategies.size()-1);

        pricingStrategy = pricingStrategies.get(randomPricingStrategyIndex);

        double totalPrice = pricingStrategy.priceProduct(placedOrder, orderedProduct);

        String orderFinalizedMessage = String.format("Order is finalized for Product %s and Quantity %d with total price %d",
                placedOrder.getProductName(), placedOrder.getQuantity(), totalPrice);

        sendMessage(orderFinalizedMessage);

        Product productAfterFulfilled = this.productService.handleGetProduct(placedOrder.getProductName());

        if(productAfterFulfilled.getCurrentStockQuantity() < productAfterFulfilled.getTargetMinStockQuantity()) {

            productAfterFulfilled.setState(new LowStockState());

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

    private void restockAfterFulfilled(Product product){
        String restockingOperationInitiatedMessage = String.format("Restocking Operation for Product %s initiated",
                product.getProductName());

        String restockingOperationCompletedMessage = String.format("Restocking Operation for Product %s completed",
                product.getProductName());

        sendMessage(restockingOperationInitiatedMessage);

        product.setState(new RestockingToFulfillOrderState());

        restockStrategy.restock(this.productService, product);

        sendMessage(restockingOperationCompletedMessage);

        product.setState(new RegularStockState());

    }

}