package factories.messageFactories;

import models.messages.Message;
import models.messages.RestockMessage;

public class RestockMessageFactory extends MessageFactory{
    /**
     * @param topic
     * @param data
     * @return
     */
    @Override
    public Message createMessage(String topic, Object data) {
        return new RestockMessage(topic, data);
    }
}
