package weike.entity.persistence;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by muyi on 17-4-6.
 */
public class ProjectInfo {

    //项目id
    private int projectId;
    //项目名字
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
    private String projectNeed;
    //项目开始时间
    private long projectStart;
    //结束时间
    private long projectEnd;
    //项目概述
    private String projectProfile;



    public ProjectInfo() {

    }

    public ProjectInfo(int projectId, String projectName, String projectKind, String projectConnector, long qq, String email, int numNeed, String projectNeed, long projectStart, long projectEnd, String projectProfile) {
        this.projectId = projectId;
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
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
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

    public String getProjectNeed() {
        return projectNeed;
    }

    public void setProjectNeed(String projectNeed) {
        this.projectNeed = projectNeed;
    }

    public long getProjectStart() {
        return projectStart;
    }

    public void setProjectStart(long projectStart) {
        this.projectStart = projectStart;
    }

    public String getProjectConnector() {
        return projectConnector;
    }

    public void setProjectConnector(String projectConnector) {
        this.projectConnector = projectConnector;
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
}
