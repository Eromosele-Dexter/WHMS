package services;

import apiContracts.Requests.PlaceOrderRequest;
import apiContracts.Responses.PlaceOrderResponse;
import controllers.WebsocketController;
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

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static statics.Endpoints.WS_PORT;

public class OrderService {

    private static ProductService productService;

    private static final Queue<Order> orders = new ConcurrentLinkedQueue<>();

    private IRestockOperationStrategy restockStrategy;

    private PricingStrategy pricingStrategy;

    private List<PricingStrategy> pricingStrategies;

    private List<IRestockOperationStrategy> restockOperationStrategies;

    private WebsocketController wsController;

    public OrderService(ProductService productService) {
        this.productService = productService;

        this.pricingStrategies = new ArrayList<>();

        this.restockOperationStrategies = new ArrayList<>();

        this.pricingStrategies.add(new PricingStrategy001());

        this.pricingStrategies.add(new PricingStrategy002());

        this.restockOperationStrategies.add(new RestockByMachine());

        this.restockOperationStrategies.add(new RestockByLabour());

        InetSocketAddress address = new InetSocketAddress(WS_PORT);

        wsController = WebsocketController.getInstance(address);
    }


    public PlaceOrderResponse handlePlaceOrder(PlaceOrderRequest request, Date date, String cookie){
        Order placedOrder = new Order(request.getProductName(), request.getQuantity(), date, cookie);

        Product orderedProduct = this.productService.handleGetProduct(placedOrder.getProductName());

        if(placedOrder.getQuantity() > orderedProduct.getTargetMaxStockQuantity()){

            PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse(placedOrder,"o");

            sendMessage("Order exceeds the max quantity set for this product: "+ placedOrder.getProductName() + " and cannot be processed");

            return placeOrderResponse;
        }

        orders.add(placedOrder);

        System.out.println("order placed");

        return handleProcessOrder();
    }

    public PlaceOrderResponse handleProcessOrder(){

        Order placedOrder = orders.poll();

        System.out.println("1djdk");

        Product orderedProduct = this.productService.handleGetProduct(placedOrder.getProductName());

        System.out.println("2djdk");

        PlaceOrderResponse placeOrderResponse = null;

        String restockingOperationInitiatedMessage = String.format("Restocking Operation for Product %s initiated",
                placedOrder.getProductName());

        String restockingOperationCompletedMessage = String.format("Restocking Operation for Product %s completed",
                placedOrder.getProductName());

        int randomRestockStrategyIndex = getRandomNumber(0, this.restockOperationStrategies.size()-1);

        restockStrategy = this.restockOperationStrategies.get(randomRestockStrategyIndex);

        int quantityRequested = placedOrder.getQuantity();

        if(placedOrder.getQuantity() > orderedProduct.getCurrentStockQuantity()) {


            String orderExceedsAvailableMessage = String.format("Order for Product %s Quantity %d is  pending – order exceeds available quantity",
                    placedOrder.getProductName(), quantityRequested);

            sendMessage(orderExceedsAvailableMessage);

            sendMessage(restockingOperationInitiatedMessage);

            restockStrategy.restock(this.productService, orderedProduct);

            sendMessage(restockingOperationCompletedMessage);
        }

        System.out.println("3djdk");

        int currentQuantity = this.productService.handleGetProduct(placedOrder.getProductName()).getCurrentStockQuantity();

        System.out.println("quantity after first restock: " + currentQuantity);

        orderedProduct.setCurrentStockQuantity(currentQuantity - quantityRequested);

        this.productService.handleUpdateProduct(orderedProduct, orderedProduct.getProductId());

        System.out.println("4djdk");

//        processOrder(); TODO: UNCOMMENT

        int randomPricingStrategyIndex = getRandomNumber(0, this.pricingStrategies.size()-1);

        int discountStrategyId = orderedProduct.getDiscountStrategyId() > this.pricingStrategies.size()-1 ? randomPricingStrategyIndex : orderedProduct.getDiscountStrategyId();

        pricingStrategy = pricingStrategies.get(discountStrategyId);

        System.out.println("5djdk");

        double totalPrice = pricingStrategy.priceProduct(placedOrder, orderedProduct);

        System.out.println("6djdk");

        String orderFinalizedMessage = String.format("Order is finalized for Product %s and Quantity %d with total price %.2f",
                placedOrder.getProductName(), placedOrder.getQuantity(), totalPrice);

        System.out.println("7djdk");

        sendMessage(orderFinalizedMessage);

        Product productAfterFulfilled = this.productService.handleGetProduct(placedOrder.getProductName());

        System.out.println("fulfilled product: " + productAfterFulfilled.getCurrentStockQuantity() );

        System.out.println("8djdk");

        if(productAfterFulfilled.getCurrentStockQuantity() < productAfterFulfilled.getTargetMinStockQuantity()) {

            productAfterFulfilled.setState(new LowStockState());

            restockAfterFulfilled(productAfterFulfilled);

        }

        System.out.println("9djdk");

        placeOrderResponse = new PlaceOrderResponse(placedOrder, "ok");

        System.out.println("after fulfilled product: " + this.productService.handleGetProduct(placedOrder.getProductName()).getCurrentStockQuantity() );

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
        System.out.println(message);
    }

    private void sendMessage(String message, String client){
        wsController.sendMessageToClient(message, client);
    }

    private void restockAfterFulfilled(Product product){

        System.out.println("Restock after fulfilled");

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
