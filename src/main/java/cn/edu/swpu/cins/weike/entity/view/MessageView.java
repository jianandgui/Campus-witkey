package cn.edu.swpu.cins.weike.entity.view;

/**
 * Created by muyi on 17-7-15.
 */
public class MessageView {

    private String projectName;
    private String content;

    public MessageView(String projectName, String content) {
        this.projectName = projectName;
        this.content = content;
    }

    public MessageView() {
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
