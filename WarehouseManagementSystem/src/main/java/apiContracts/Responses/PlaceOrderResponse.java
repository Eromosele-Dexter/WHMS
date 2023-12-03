package apiContracts.Responses;

import models.order.Order;
import utils.JsonUtils;

public class PlaceOrderResponse {

    private Order placedOrder;

    private String orderResponse;

    public PlaceOrderResponse(Order placedOrder, String orderResponse) {
        this.placedOrder = placedOrder;
        this.orderResponse = orderResponse;
    }

    public Order getPlacedOrder() {
        return placedOrder;
    }

    public String getOrderResponse() {
        return orderResponse;
    }

    @Override
    public String toString() {
        return JsonUtils.convertObjectToJson(this);
    }
}
