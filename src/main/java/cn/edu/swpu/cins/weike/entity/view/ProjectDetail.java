package cn.edu.swpu.cins.weike.entity.view;

import lombok.Data;

import java.util.List;

/**
 * Created by muyi on 17-5-21.
 */
@Data
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



    public ProjectDetail() {
    }


}
