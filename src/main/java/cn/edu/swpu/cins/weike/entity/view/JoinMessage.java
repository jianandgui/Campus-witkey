package cn.edu.swpu.cins.weike.entity.view;


import lombok.Data;

@Data
public class JoinMessage {

    private String projectAbout;
    private String projectApplicant;

    public JoinMessage(String projectAbout, String projectApplicant) {
        this.projectAbout = projectAbout;
        this.projectApplicant = projectApplicant;
    }

    public JoinMessage() {
    }


}
