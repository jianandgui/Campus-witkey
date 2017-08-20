package cn.edu.swpu.cins.weike.entity.view;

import lombok.Data;

import java.util.List;

/**
 * Created by muyi on 17-7-16.
 */
@Data
public class PersonData {

    private int id;
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
    private long entryUniversity;
    //毕业时间
    private long leaveUniversity;
    private long qq;
    //拥有技能
    private List<String> skills;
    //项目经验
    private String experience;
    //自我评价
    private String selfFeel;
    //学生等级
    private String level;
    private String email;
    private String role;

    public PersonData(int id, String username, String image, String sex, String eduBackgroud, String university, String majorAndGrade, long entryUniversity, long leaveUniversity, long qq, List<String> skills, String experience, String selfFeel, String level, String email, String role) {
        this.id = id;
        this.username = username;
        this.image = image;
        this.sex = sex;
        this.eduBackgroud = eduBackgroud;
        this.university = university;
        this.majorAndGrade = majorAndGrade;
        this.entryUniversity = entryUniversity;
        this.leaveUniversity = leaveUniversity;
        this.qq = qq;
        this.skills = skills;
        this.experience = experience;
        this.selfFeel = selfFeel;
        this.level = level;
        this.email = email;
        this.role = role;
    }

    public PersonData() {
    }
}
