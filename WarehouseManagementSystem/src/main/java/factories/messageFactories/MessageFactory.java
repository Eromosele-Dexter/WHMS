package factories.messageFactories;

import models.messages.Message;

public abstract class MessageFactory {
    public abstract Message createMessage(String topic, Object data);
}
