package services;

import apiContracts.Requests.PlaceOrderRequest;
import apiContracts.Responses.PlaceOrderResponse;
import controllers.WebsocketController;
import factories.messageFactories.*;
import factories.productFactories.ProductFactory;
import models.messages.Message;
import models.order.Order;
import models.products.Product;
import productStates.LowStockState;
import productStates.RegularStockState;
import productStates.RestockingToFulfillOrderState;
import statics.factoryKeys;
import statics.messageTopics;
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
import static statics.SessionKeys.ADMIN_COOKIE;

public class OrderService {

    private static ProductService productService;

    private static final Queue<Order> orders = new ConcurrentLinkedQueue<>();

    private IRestockOperationStrategy restockStrategy;

    private PricingStrategy pricingStrategy;

    private List<PricingStrategy> pricingStrategies;

    private List<IRestockOperationStrategy> restockOperationStrategies;

    private WebsocketController wsController;

    private Map<String, MessageFactory> messageFactoryMap;

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

        this.messageFactoryMap = new HashMap<>();

        this.messageFactoryMap.put(factoryKeys.CURRENT_STOCK_FACTORY, new CurrentStockMessageFactory());

        this.messageFactoryMap.put(factoryKeys.GENERAL_ORDER_FACTORY, new GeneralOrderMessageFactory());

        this.messageFactoryMap.put(factoryKeys.LAST_ORDER_FACTORY, new LastOrderMessageFactory());

        this.messageFactoryMap.put(factoryKeys.RESTOCK_FACTORY, new RestockMessageFactory());
    }


    public PlaceOrderResponse handlePlaceOrder(PlaceOrderRequest request, Date date, String clientCookie){

        String productName = request.getProductName();

        int requestedQuantity = request.getQuantity();

        MessageFactory messageFactory = this.messageFactoryMap.get(factoryKeys.GENERAL_ORDER_FACTORY);

        Message orderSentMessage = messageFactory.createMessage(messageTopics.GENERAL, String.format("Order for Product %s, Quantity %d is sent", productName, requestedQuantity));

        sendMessage(orderSentMessage.toString(), clientCookie);

        Order placedOrder = new Order(productName, requestedQuantity, date, clientCookie);

        Product orderedProduct = this.productService.handleGetProduct(placedOrder.getProductName());

        messageFactory = this.messageFactoryMap.get(factoryKeys.GENERAL_ORDER_FACTORY);

        Message orderRecievedMessage = messageFactory.createMessage(messageTopics.GENERAL, String.format("Order is received for Product %s and Quantity %d", productName, requestedQuantity));

        sendMessage(orderRecievedMessage.toString(), ADMIN_COOKIE);

        if(placedOrder.getQuantity() > orderedProduct.getTargetMaxStockQuantity()){

            PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse(placedOrder,"Order processed.");

            messageFactory = this.messageFactoryMap.get(factoryKeys.GENERAL_ORDER_FACTORY);

            Message orderExceedsMessage = messageFactory.createMessage(messageTopics.GENERAL, String.format("Order for Product %s Quantity %d exceeds the max quantity set for this product and cannot be processed",
                    placedOrder.getProductName(), placedOrder.getQuantity()));


            sendMessage(orderExceedsMessage.toString(), ADMIN_COOKIE);

            sendMessage(orderExceedsMessage.toString(), clientCookie);

            return placeOrderResponse;
        }

        orders.add(placedOrder);

        return handleProcessOrder();
    }

    public PlaceOrderResponse handleProcessOrder(){

        Order placedOrder = orders.poll();

        Product orderedProduct = this.productService.handleGetProduct(placedOrder.getProductName());

        PlaceOrderResponse placeOrderResponse = null;

        MessageFactory messageFactory = this.messageFactoryMap.get(factoryKeys.RESTOCK_FACTORY);

        Message restockingOperationInitiatedMessage  = messageFactory.createMessage(messageTopics.RESTOCK, String.format("Restocking Operation for Product %s initiated",
                placedOrder.getProductName()));

        Message restockingOperationCompletedMessage  = messageFactory.createMessage(messageTopics.RESTOCK, String.format("Restocking Operation for Product %s completed",
                placedOrder.getProductName()));

        int randomRestockStrategyIndex = getRandomNumber(0, this.restockOperationStrategies.size()-1);

        restockStrategy = this.restockOperationStrategies.get(randomRestockStrategyIndex);

        int quantityRequested = placedOrder.getQuantity();

        if(placedOrder.getQuantity() > orderedProduct.getCurrentStockQuantity()) {

            messageFactory = this.messageFactoryMap.get(factoryKeys.GENERAL_ORDER_FACTORY);

            Message orderExceedsAvailableMessage = messageFactory.createMessage(messageTopics.GENERAL, String.format("Order for Product %s Quantity %d is  pending – order exceeds available quantity",
                    placedOrder.getProductName(), quantityRequested));

            sendMessage(orderExceedsAvailableMessage.toString(), placedOrder.getClientCookie());

            sendMessage(restockingOperationInitiatedMessage.toString(), placedOrder.getClientCookie());

            restockStrategy.restock(this.productService, orderedProduct);

            sendMessage(restockingOperationCompletedMessage.toString(), placedOrder.getClientCookie());
        }

        int currentQuantity = this.productService.handleGetProduct(placedOrder.getProductName()).getCurrentStockQuantity();

        orderedProduct.setCurrentStockQuantity(currentQuantity - quantityRequested);

        this.productService.handleUpdateProduct(orderedProduct, orderedProduct.getProductId());

//        processOrder();// TODO: uncomment

        int randomPricingStrategyIndex = getRandomNumber(0, this.pricingStrategies.size()-1);

        int discountStrategyId = orderedProduct.getDiscountStrategyId() > this.pricingStrategies.size()-1 ? randomPricingStrategyIndex : orderedProduct.getDiscountStrategyId();

        pricingStrategy = pricingStrategies.get(discountStrategyId);

        double totalPrice = pricingStrategy.priceProduct(placedOrder, orderedProduct);


        messageFactory = this.messageFactoryMap.get(factoryKeys.GENERAL_ORDER_FACTORY);

        Message orderFinalizedMessage = messageFactory.createMessage(messageTopics.GENERAL, String.format("Order is finalized for Product %s and Quantity %d with total price %.2f",
                placedOrder.getProductName(), placedOrder.getQuantity(), totalPrice));

        sendMessage(orderFinalizedMessage.toString(), placedOrder.getClientCookie());

        messageFactory = this.messageFactoryMap.get(factoryKeys.LAST_ORDER_FACTORY);

        Message lastOrderMessage = messageFactory.createMessage(messageTopics.LAST_ORDER, placedOrder);

        sendMessage(lastOrderMessage.toString(), ADMIN_COOKIE);

        Product productAfterFulfilled = this.productService.handleGetProduct(placedOrder.getProductName());

        messageFactory = this.messageFactoryMap.get(factoryKeys.CURRENT_STOCK_FACTORY);

        Message currentStockUpdateMessage = messageFactory.createMessage(messageTopics.UPDATED_CURRENT_STOCK,this.productService.handleRetrieveAllProducts());

        sendMessage(currentStockUpdateMessage.toString(), ADMIN_COOKIE);

        if(productAfterFulfilled.getCurrentStockQuantity() < productAfterFulfilled.getTargetMinStockQuantity()) {

            productAfterFulfilled.setState(new LowStockState());

            restockAfterFulfilled(productAfterFulfilled);
        }

        currentStockUpdateMessage = messageFactory.createMessage(messageTopics.UPDATED_CURRENT_STOCK,this.productService.handleRetrieveAllProducts());

        sendMessage(currentStockUpdateMessage.toString(), ADMIN_COOKIE);

        placeOrderResponse = new PlaceOrderResponse(placedOrder, "ok");


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

//    private void sendMessage(String message){
//        System.out.println(message);
//        wsController.sendMessageToClient(message, ADMIN_COOKIE);
//    }

    private void sendMessage(String message, String wsClient){
        wsController.sendMessageToClient(message, wsClient);
    }

    private void restockAfterFulfilled(Product product){

        MessageFactory messageFactory = this.messageFactoryMap.get(factoryKeys.RESTOCK_FACTORY);

        Message restockingOperationInitiatedMessage  = messageFactory.createMessage(messageTopics.RESTOCK, String.format("Restocking Operation for Product %s initiated",
                product.getProductName()));

        Message restockingOperationCompletedMessage  = messageFactory.createMessage(messageTopics.RESTOCK, String.format("Restocking Operation for Product %s completed",
                product.getProductName()));


        sendMessage(restockingOperationInitiatedMessage.toString(), ADMIN_COOKIE);

        product.setState(new RestockingToFulfillOrderState());

        restockStrategy.restock(this.productService, product);

        sendMessage(restockingOperationCompletedMessage.toString(), ADMIN_COOKIE);

        product.setState(new RegularStockState());

    }

}
