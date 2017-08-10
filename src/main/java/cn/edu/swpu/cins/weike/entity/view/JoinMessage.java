package cn.edu.swpu.cins.weike.entity.view;



public class JoinMessage {

    private String projectAbout;
    private String projectApplicant;

    public String getProjectAbout() {
        return projectAbout;
    }

    public void setProjectAbout(String projectAbout) {
        this.projectAbout = projectAbout;
    }

    public JoinMessage(String projectAbout, String projectApplicant) {
        this.projectAbout = projectAbout;
        this.projectApplicant = projectApplicant;
    }

    public JoinMessage() {
    }

    public String getProjectApplicant() {
        return projectApplicant;
    }

    public void setProjectApplicant(String projectApplicant) {
        this.projectApplicant = projectApplicant;
    }
}
