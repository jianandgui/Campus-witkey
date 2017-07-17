package cn.edu.swpu.cins.weike.entity.view;

/**
 * Created by muyi on 17-7-16.
 */
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        this.academy = academy;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
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

    @Override
    public String toString() {
        return "TeacherPersonData{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", image='" + image + '\'' +
                ", sex='" + sex + '\'' +
                ", university='" + university + '\'' +
                ", academy='" + academy + '\'' +
                ", rank='" + rank + '\'' +
                ", qq=" + qq +
                ", email='" + email + '\'' +
                '}';
    }
}
