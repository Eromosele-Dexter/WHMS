package factories.messageFactories;

import models.messages.CurrentStockMessage;
import models.messages.Message;

public class CurrentStockMessageFactory extends MessageFactory{
    /**
     * @param topic
     * @param data
     * @return
     */
    @Override
    public Message createMessage(String topic, Object data) {
       return new CurrentStockMessage(topic, data);
    }
}
