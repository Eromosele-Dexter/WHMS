package factories.messageFactories;

import models.messages.GeneralOrderMessage;
import models.messages.Message;

public class GeneralOrderMessageFactory extends MessageFactory{
    /**
     * @param topic
     * @param data
     * @return
     */
    @Override
    public Message createMessage(String topic, Object data) {
       return new GeneralOrderMessage(topic, data);
    }
}
