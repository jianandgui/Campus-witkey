package cn.edu.swpu.cins.weike.entity.persistence;



import lombok.Data;

import java.util.Date;

/**
 * Created by muyi on 17-6-12.
 */
@Data
public class Message {
    private int id;
    private String fromName;
    private String toName;
    private String content;
    private Date createDate;
    private int hasRead;
    private String projectAbout;

    public Message(int id, String fromName, String toName, String content, Date createDate, int hasRead, String projectAbout) {
        this.id = id;
        this.fromName = fromName;
        this.toName = toName;
        this.content = content;
        this.createDate = createDate;
        this.hasRead = hasRead;
        this.projectAbout = projectAbout;
    }

    public Message() {
    }


}
