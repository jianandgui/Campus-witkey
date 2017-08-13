package cn.edu.swpu.cins.weike.entity.view;

import cn.edu.swpu.cins.weike.entity.persistence.Message;
import lombok.Data;

import java.util.List;

/**
 * Created by muyi on 17-7-15.
 */
@Data
public class MessageList {

    private List<Message> fromMessages;
    private List<Message> toMessages;

    public MessageList(List<Message> fromMessages, List<Message> toMessages) {
        this.fromMessages = fromMessages;
        this.toMessages = toMessages;
    }

    public MessageList() {
    }


}
