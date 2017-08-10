package cn.edu.swpu.cins.weike.entity.view;

/**
 * Created by muyi on 17-7-15.
 */
public class MessageView {

    private String projectName;
    private String content;
    private String projectAbout;

    public MessageView(String projectName, String content, String projectAbout) {
        this.projectName = projectName;
        this.content = content;
        this.projectAbout = projectAbout;
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

    public String getProjectAbout() {
        return projectAbout;
    }

    public void setProjectAbout(String projectAbout) {
        this.projectAbout = projectAbout;
    }
}
