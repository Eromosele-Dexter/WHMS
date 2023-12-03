package models.messages;

import utils.JsonUtils;

public class RestockMessage extends Message{
    public RestockMessage(String topic, Object data) {
        super(topic, data);
    }

    @Override
    public String toString(){
        return JsonUtils.convertObjectToJson(this);
    }
}
