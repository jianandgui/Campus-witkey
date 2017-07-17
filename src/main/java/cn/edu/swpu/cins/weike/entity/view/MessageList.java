package cn.edu.swpu.cins.weike.entity.view;

import cn.edu.swpu.cins.weike.entity.persistence.Message;

import java.util.List;

/**
 * Created by muyi on 17-7-15.
 */
public class MessageList {

    private List<Message> fromMessages;
    private List<Message> toMessages;

    public MessageList(List<Message> fromMessages, List<Message> toMessages) {
        this.fromMessages = fromMessages;
        this.toMessages = toMessages;
    }

    public MessageList() {
    }

    public List<Message> getFromMessages() {
        return fromMessages;
    }

    public void setFromMessages(List<Message> fromMessages) {
        this.fromMessages = fromMessages;
    }

    public List<Message> getToMessages() {
        return toMessages;
    }

    public void setToMessages(List<Message> toMessages) {
        this.toMessages = toMessages;
    }

    @Override
    public String toString() {
        return "MessageList{" +
                "fromMessages=" + fromMessages +
                ", toMessages=" + toMessages +
                '}';
    }
}
