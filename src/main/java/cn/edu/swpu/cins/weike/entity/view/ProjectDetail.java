package cn.edu.swpu.cins.weike.entity.view;

import java.util.List;

/**
 * Created by muyi on 17-5-21.
 */
public class ProjectDetail {

    private String projectName;
    //项目类型
    private String projectKind;
    //项目联系人
    private String projectConnector;
    //联系人电话
    private long qq;
    private String email;
    //项目需要人数
    private int numNeed;
    //项目技能要求
    private List<String> projectNeed;
    //项目开始时间
    private long projectStart;
    //结束时间
    private long projectEnd;
    //项目概述
    private String projectProfile;
    //访问量
    private long proHits;

    public ProjectDetail(String projectName, String projectKind, String projectConnector, long qq, String email, int numNeed, List<String> projectNeed, long projectStart, long projectEnd, String projectProfile, long proHits) {
        this.projectName = projectName;
        this.projectKind = projectKind;
        this.projectConnector = projectConnector;
        this.qq = qq;
        this.email = email;
        this.numNeed = numNeed;
        this.projectNeed = projectNeed;
        this.projectStart = projectStart;
        this.projectEnd = projectEnd;
        this.projectProfile = projectProfile;
        this.proHits = proHits;
    }

    public ProjectDetail() {
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectKind() {
        return projectKind;
    }

    public void setProjectKind(String projectKind) {
        this.projectKind = projectKind;
    }

    public String getProjectConnector() {
        return projectConnector;
    }

    public void setProjectConnector(String projectConnector) {
        this.projectConnector = projectConnector;
    }

    public long getQq() {
        return qq;
    }

    public void setQq(long qq) {
        this.qq = qq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNumNeed() {
        return numNeed;
    }

    public void setNumNeed(int numNeed) {
        this.numNeed = numNeed;
    }

    public List<String> getProjectNeed() {
        return projectNeed;
    }

    public void setProjectNeed(List<String> projectNeed) {
        this.projectNeed = projectNeed;
    }

    public long getProjectStart() {
        return projectStart;
    }

    public void setProjectStart(long projectStart) {
        this.projectStart = projectStart;
    }

    public long getProjectEnd() {
        return projectEnd;
    }

    public void setProjectEnd(long projectEnd) {
        this.projectEnd = projectEnd;
    }

    public String getProjectProfile() {
        return projectProfile;
    }

    public void setProjectProfile(String projectProfile) {
        this.projectProfile = projectProfile;
    }

    public long getProHits() {
        return proHits;
    }

    public void setProHits(long proHits) {
        this.proHits = proHits;
    }
}
