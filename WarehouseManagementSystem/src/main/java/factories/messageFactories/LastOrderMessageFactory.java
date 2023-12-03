package factories.messageFactories;

import models.messages.LastOrderMessage;
import models.messages.Message;

public class LastOrderMessageFactory extends MessageFactory{
    /**
     * @param topic
     * @param data
     * @return
     */
    @Override
    public Message createMessage(String topic, Object data) {
       return new LastOrderMessage(topic, data);
    }
}
