package cn.edu.swpu.cins.weike.entity.persistence;

import java.util.Date;

/**
 * Created by muyi on 17-6-12.
 */
public class Message {

    private int id;
    private String fromName;
    private String toName;
    private String content;
    private Date createDate;
    private int hasRead;
    private String conversationId;

    public Message(int id, String fromName, String toName, String content, Date createDate, int hasRead, String conversationId) {
        this.id = id;
        this.fromName = fromName;
        this.toName = toName;
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

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
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
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
