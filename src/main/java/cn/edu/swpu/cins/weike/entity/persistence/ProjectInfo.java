package cn.edu.swpu.cins.weike.entity.persistence;



import lombok.Data;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by muyi on 17-4-6.
 */
@Data
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
    private List<String> projectNeed;
    //项目开始时间
    private long projectStart;
    //结束时间
    private long projectEnd;
    //项目概述
    private String projectProfile;


    public ProjectInfo() {
    }
    public ProjectInfo(int projectId, String projectName, String projectKind, String projectConnector, long qq, String email, int numNeed, List<String> projectNeed, long projectStart, long projectEnd, String projectProfile) {
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

    @Override
    public String toString() {
        return "ProjectInfo{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", projectKind='" + projectKind + '\'' +
                ", projectConnector='" + projectConnector + '\'' +
                ", qq=" + qq +
                ", email='" + email + '\'' +
                ", numNeed=" + numNeed +
                ", projectNeed=" + projectNeed +
                ", projectStart=" + projectStart +
                ", projectEnd=" + projectEnd +
                ", projectProfile='" + projectProfile + '\'' +
                '}';
    }
}
