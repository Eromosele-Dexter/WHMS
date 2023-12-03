package models.messages;

import utils.JsonUtils;

public class GeneralOrderMessage extends Message{
    public GeneralOrderMessage(String topic, Object data) {
        super(topic, data);
    }

    @Override
    public String toString(){
        return JsonUtils.convertObjectToJson(this);
    }
}
