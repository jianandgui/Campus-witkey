package cn.edu.swpu.cins.weike.entity.view;

import lombok.Data;

/**
 * Created by muyi on 17-7-16.
 */
@Data
public class TeacherPersonData {

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

    private String email;

    public TeacherPersonData(int id, String username, String image, String sex, String university, String academy, String rank, long qq, String email) {
        this.id = id;
        this.username = username;
        this.image = image;
        this.sex = sex;
        this.university = university;
        this.academy = academy;
        this.rank = rank;
        this.qq = qq;
        this.email = email;
    }

    public TeacherPersonData() {
    }

}
