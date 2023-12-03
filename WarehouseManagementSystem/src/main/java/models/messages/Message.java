package models.messages;

public abstract class Message {

    protected String topic;

    protected Object data;


    public Message(String topic, Object data) {
        this.topic = topic;
        this.data = data;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
