package cn.edu.swpu.cins.weike.entity.view;

import lombok.Data;

/**
 * Created by muyi on 17-7-15.
 */
@Data
public class MessageView {

    private String projectName;
    private String content;

    public MessageView(String projectName, String content) {
        this.projectName = projectName;
        this.content = content;
    }

    public MessageView() {
    }

}
