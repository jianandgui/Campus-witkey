package cn.edu.swpu.cins.weike.entity.persistence;

import java.sql.Date;
import java.util.List;

/**
 * Created by muyi on 17-4-24.
 */
public class StudentDetail {


    //这个类用于学生用户注册后填写个人信息 允许不填 不过不能参加项目或其他项目功能

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


    public StudentDetail() {
    }

    @Override
    public String toString() {
        return "StudentDetail{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", image='" + image + '\'' +
                ", sex='" + sex + '\'' +
                ", eduBackgroud='" + eduBackgroud + '\'' +
                ", university='" + university + '\'' +
                ", majorAndGrade='" + majorAndGrade + '\'' +
                ", entryUniversity=" + entryUniversity +
                ", leaveUniversity=" + leaveUniversity +
                ", qq=" + qq +
                ", skills=" + skills +
                ", experience='" + experience + '\'' +
                ", selfFeel='" + selfFeel + '\'' +
                ", level='" + level + '\'' +
                '}';
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }


    public long getQq() {
        return qq;
    }

    public void setQq(long qq) {
        this.qq = qq;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getEduBackgroud() {
        return eduBackgroud;
    }

    public void setEduBackgroud(String eduBackgroud) {
        this.eduBackgroud = eduBackgroud;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getMajorAndGrade() {
        return majorAndGrade;
    }

    public void setMajorAndGrade(String majorAndGrade) {
        this.majorAndGrade = majorAndGrade;
    }

    public long getEntryUniversity() {
        return entryUniversity;
    }

    public void setEntryUniversity(long entryUniversity) {
        this.entryUniversity = entryUniversity;
    }

    public long getLeaveUniversity() {
        return leaveUniversity;
    }

    public void setLeaveUniversity(long leaveUniversity) {
        this.leaveUniversity = leaveUniversity;
    }

    public String getSelfFeel() {
        return selfFeel;
    }

    public void setSelfFeel(String selfFeel) {
        this.selfFeel = selfFeel;
    }

    public StudentDetail(int id, String username, String image, String sex, String eduBackgroud, String university, String majorAndGrade, long entryUniversity, long leaveUniversity, long qq, List<String> skills, String experience, String selfFeel, String level) {
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
    }
}
