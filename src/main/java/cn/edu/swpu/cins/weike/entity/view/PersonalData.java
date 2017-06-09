package cn.edu.swpu.cins.weike.entity.view;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by muyi on 17-4-9.
 */
@Getter
@Setter
public class PersonalData {

    //个人资料类

    //所有人有权限查看某个人的个人资料
    private String username;
    private String image;
    private String sex;
    //学生学历
    private String eduBackgroud;
    //学生所在大学
    private String university;
    //学生专业年级
    private String majorAndGrade;
    //入学时间
    private Timestamp entryUniversity;
    //毕业时间
    private Timestamp leaveUniversity;
    private long qq;
    private String email;
    //学生专业技能
    private String skills;
    //项目经验
    private String experience;


}
