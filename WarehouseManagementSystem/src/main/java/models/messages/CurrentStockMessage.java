package models.messages;

import utils.JsonUtils;

public class CurrentStockMessage extends Message{
    public CurrentStockMessage(String topic, Object data) {
        super(topic, data);
    }

    @Override
    public String toString(){
        return JsonUtils.convertObjectToJson(this);
    }
}
