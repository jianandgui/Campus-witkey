package cn.edu.swpu.cins.weike.entity.persistence;


import lombok.Data;

/**
 * Created by muyi on 17-4-24.
 */
@Data
public class TeacherDetail {

    //这个类用于用户注册后填写个人信息 允许不填 不过不能参加项目或其他项目功能
    private int id;
    private String username;
    private String image;
    private String sex;
    //老师所在大学
    private String university;
    //老师所在学院
    private String academy;
    //老师职称
    private String rank;
    private long qq;

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public TeacherDetail(int id, String username, String image, String sex, String university, String academy, String rank, long qq) {

        this.id = id;
        this.username = username;
        this.image = image;
        this.sex = sex;
        this.university = university;
        this.academy = academy;
        this.rank = rank;
        this.qq = qq;
    }

    public TeacherDetail() {
    }

}
