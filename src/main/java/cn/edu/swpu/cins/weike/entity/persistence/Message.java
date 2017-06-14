package cn.edu.swpu.cins.weike.entity.persistence;

import java.util.Date;

/**
 * Created by muyi on 17-6-12.
 */
public class Message {

    private int id;
    private int fromId;
    private int toId;
    private String content;
    private Date createDate;
    private int hasRead;
    private String conversationId;

    public Message(int id, int fromId, int toId, String content, Date createDate, int hasRead, String conversationId) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.content = content;
        this.createDate = createDate;
        this.hasRead = hasRead;
        this.conversationId = conversationId;
    }

    public Message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    public String getConversationId() {
        if(fromId<toId){
            return String.format("%d_%d",fromId,toId);
        }
        else
            return String.format("%d_%d",toId,fromId);
    }

    public void setConversationId(String conversationId) {
        this.conversationId=conversationId;
    }
}
