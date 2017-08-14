package cn.edu.swpu.cins.weike.entity.persistence;


import lombok.Data;

/**
 * Created by muyi on 17-4-6.
 */
@Data
public class TeacherInfo {
    //老师id
    private int id;
    //用户名
    private String username;
    //用户密码
    private String password;
    private String email;
    private String role;
    private long lastPasswordResetDate;

    public TeacherInfo() {
    }

    public TeacherInfo(int id, String username, String password, String email, String role, long lastPasswordResetDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

}