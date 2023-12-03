package models.messages;

import utils.JsonUtils;

public class LastOrderMessage extends Message{
    public LastOrderMessage(String topic, Object data) {
        super(topic, data);
    }

    @Override
    public String toString(){
        return JsonUtils.convertObjectToJson(this);
    }
}
