package cn.edu.swpu.cins.weike.entity.view;

import lombok.Data;

/**
 * Created by muyi on 17-4-8.
 */
@Data
public class ProjectView {

    //用于首页显示所有项目，但是不是所有项目信息 只是概要
    //项目名字
    private String projectName;
    //项目概述
    private String projectProfile;
    //项目发起人
    private String projectConnector;
    //项目负责人QQ
    private long qq;
    private String email;

    public ProjectView(String projectName, String projectProfile, String projectConnector, long qq, String email) {
        this.projectName = projectName;
        this.projectProfile = projectProfile;
        this.projectConnector = projectConnector;
        this.qq = qq;
        this.email = email;
    }

    public ProjectView() {
    }
}
