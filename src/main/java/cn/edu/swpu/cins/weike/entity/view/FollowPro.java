package cn.edu.swpu.cins.weike.entity.view;

import lombok.Data;

@Data
public class FollowPro {

    private String projectName;
    private String projectConnector;

    public FollowPro(String projectName, String projectConnector) {
        this.projectName = projectName;
        this.projectConnector = projectConnector;
    }

    public FollowPro() {
    }


}
