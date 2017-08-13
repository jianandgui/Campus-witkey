package cn.edu.swpu.cins.weike.entity.view;

public class FollowPro {

    private String projectName;
    private String projectConnector;

    public FollowPro(String projectName, String projectConnector) {
        this.projectName = projectName;
        this.projectConnector = projectConnector;
    }

    public String getProjectConnector() {

        return projectConnector;
    }

    public void setProjectConnector(String projectConnector) {
        this.projectConnector = projectConnector;
    }

    public FollowPro() {
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
